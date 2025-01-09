package com.depop.cx.drc.workflow.rest;

import org.camunda.bpm.engine.rest.mapper.MultipartFormData;

import java.nio.charset.StandardCharsets;

class WritableFormPart extends MultipartFormData.FormPart {

    protected final String fieldName;
    protected final String contentType;
    protected final String textContent;
    protected final String fileName;
    protected final byte[] binaryContent;

    public WritableFormPart(String fieldName, String contentType, String textContent) {
        this.fieldName = fieldName;
        this.contentType = contentType;
        this.textContent = textContent;
        this.fileName = null;
        this.binaryContent = null;
    }

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public String getTextContent() {
        return textContent;
    }

    @Override
    public byte[] getBinaryContent() {
        return textContent.getBytes(StandardCharsets.UTF_8);
    }

    @Override
    public String getFileName() {
        return fileName;
    }

}
