/*Copyright (c) 2020-2021 fico.com All Rights Reserved.
 This software is the confidential and proprietary information of fico.com You shall not disclose such Confidential Information and shall use it only in accordance
 with the terms of the source code license agreement you entered into with fico.com*/
package com.fico.dmp.telusagentuidb.models.query;

/*This is a Studio Managed File. DO NOT EDIT THIS FILE. Your changes may be reverted by Studio.*/


import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.wavemaker.commons.json.serializer.ByteArrayToStringSerializer;
import com.wavemaker.commons.json.views.JsonViews.BlobAsUrlView;
import com.wavemaker.runtime.data.annotations.ColumnAlias;

public class GetDocumentByDocIdResponse implements Serializable {


    @ColumnAlias("ID")
    private Long id;

        @JsonView(BlobAsUrlView.class)
    @JsonSerialize(using = ByteArrayToStringSerializer.class)
    @JsonProperty(access = Access.READ_ONLY)
    @ColumnAlias("document")
    private byte[] document;

    @ColumnAlias("documentName")
    private String documentName;

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public byte[] getDocument() {
        return this.document;
    }

    public void setDocument(byte[] document) {
        this.document = document;
    }

    public String getDocumentName() {
        return this.documentName;
    }

    public void setDocumentName(String documentName) {
        this.documentName = documentName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof GetDocumentByDocIdResponse)) return false;
        final GetDocumentByDocIdResponse getDocumentByDocIdResponse = (GetDocumentByDocIdResponse) o;
        return Objects.equals(getId(), getDocumentByDocIdResponse.getId()) &&
                Objects.equals(getDocument(), getDocumentByDocIdResponse.getDocument()) &&
                Objects.equals(getDocumentName(), getDocumentByDocIdResponse.getDocumentName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
                getDocument(),
                getDocumentName());
    }
}