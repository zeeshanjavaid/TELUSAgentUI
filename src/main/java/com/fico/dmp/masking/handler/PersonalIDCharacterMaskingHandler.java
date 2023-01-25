/*Copyright (c) 2021-2022 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/


package com.fico.dmp.masking.handler;

import com.wavemaker.runtime.mask.core.handler.IMaskingHandler;
import com.wavemaker.runtime.mask.core.model.MaskableBeanPropertyContext;
import com.wavemaker.runtime.mask.core.model.ValueWrapper;
import com.wavemaker.runtime.mask.core.processor.IValueProcessor;
import com.wavemaker.runtime.mask.core.processor.chain.ValueProcessorChain;
import java.util.Iterator;
import java.util.function.Function;

public class PersonalIDCharacterMaskingHandler implements IMaskingHandler<MaskableBeanPropertyContext> {
 
    public Object doMasking(MaskableBeanPropertyContext context) {
        
        String rawValue = context.getValue().toString();
        String maskedValue = rawValue;
        
       if(context != null && context.getValue()!=null){
           
           
           if(rawValue !=null && rawValue.length() > 4) {
               
               maskedValue = "****" + rawValue.substring(4);
               return maskedValue;

               
           }
       }

        return maskedValue;
    }

}