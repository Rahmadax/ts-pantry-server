package com.depop.cx.drc.workflow.rest;

import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;

public class TextAttachmentDto extends AttachmentDto {

    protected String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public TextAttachmentDto() {
        //no-op
    }

    public static TextAttachmentDto fromAttachment(AttachmentDto attachment) {
        TextAttachmentDto taDto = new TextAttachmentDto();
        taDto.setId(attachment.getId());
        taDto.setName(attachment.getName());
        taDto.setType(attachment.getType());
        taDto.setDescription(attachment.getDescription());
        taDto.setTaskId(attachment.getTaskId());
        taDto.setUrl(attachment.getUrl());
        taDto.setCreateTime(attachment.getCreateTime());
        taDto.setRemovalTime(attachment.getRemovalTime());
        taDto.setRootProcessInstanceId(attachment.getRootProcessInstanceId());
        return taDto;
    }

}
