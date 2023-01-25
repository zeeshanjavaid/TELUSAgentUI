package com.fico.pscomponent.service.property;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.wavemaker.runtime.prefab.core.PrefabRegistry;
import com.wavemaker.runtime.property.AppPropertySource;
import com.wavemaker.runtime.property.FawbPropertySource;
import com.wavemaker.runtime.property.impl.DbBasedPropertySource;

@Component
public class FawbPropertySourceRefresher{
    
    private static final Logger logger = LoggerFactory.getLogger(FawbPropertySourceRefresher.class);

    @Autowired
    private FawbPropertySource fawbPropertySource;
    @Autowired
    private AppPropertySource appPropertySource;
    @Autowired
    private PrefabRegistry prefabRegistry;
    
    public void refresh() {
        
        logger.info("appPropertySource.isConfigured():::::::::::::::::::::{}", appPropertySource.isConfigured());
        if (appPropertySource.isConfigured() && reinitializeSource()) {
            appPropertySource.init();
            reloadPrefabSources();
        }
    }
    
    private boolean reloadPrefabSources() {
        if(prefabRegistry != null && prefabRegistry.getPrefabs() != null)
        for (String prefabName : prefabRegistry.getPrefabs()) {
            ((AppPropertySource)
                    prefabRegistry.
                            getPrefabContext(prefabName).
                            getEnvironment().
                            getPropertySources().
                            get(AppPropertySource.CUSTOM_APP_PROPERTY_SOURCE)).
                    init();
        }
        return true;
    }

    private boolean reinitializeSource() {
        logger.info("fawbPropertySource instanceof DbBasedPropertySource::::::::::::::::::::{}", fawbPropertySource instanceof DbBasedPropertySource);
        if (fawbPropertySource instanceof DbBasedPropertySource){
            DbBasedPropertySource customPropertySource = (DbBasedPropertySource) fawbPropertySource;
            customPropertySource.init();
            return true;
        }
        return false;
    }
}
