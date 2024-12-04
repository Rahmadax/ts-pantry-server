package com.depop.cx.drc.workflow.rest;

import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;

public class TextAttachmentDto extends AttachmentDto {

    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
