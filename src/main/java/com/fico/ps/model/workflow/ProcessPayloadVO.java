package com.fico.ps.model.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wavemaker.runtime.security.xss.XssDisable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ProcessPayloadVO {

    private DownloadableContentVO content;

    public DownloadableContentVO getContent() {
        return content;
    }

    public void setContent(DownloadableContentVO content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "ProcessPayloadVO{" +
                "content=" + content +
                '}';
    }
}
