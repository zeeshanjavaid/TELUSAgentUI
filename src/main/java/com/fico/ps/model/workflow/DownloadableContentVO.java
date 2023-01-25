package com.fico.ps.model.workflow;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wavemaker.runtime.security.xss.XssDisable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DownloadableContentVO {

    private String downloadLink;
    private String value;

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DownloadableContentVO{" +
                "downloadLink='" + downloadLink + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
