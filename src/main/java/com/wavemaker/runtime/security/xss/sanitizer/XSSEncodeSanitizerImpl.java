package com.wavemaker.runtime.security.xss.sanitizer;

import org.springframework.web.util.HtmlUtils;

public class XSSEncodeSanitizerImpl extends XSSEncodeSanitizer{
    @Override
    public String sanitizeRequestData(final String data) {

        return data == null ? data : HtmlUtils.htmlEscape(data, "UTF-8");
    }
}
