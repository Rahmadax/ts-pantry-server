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

    }

    public static TextAttachmentDto fromAttachment(AttachmentDto attachment) {
        TextAttachmentDto taDto = new TextAttachmentDto();
        taDto.id = attachment.getId();
        taDto.name = attachment.getName();
        taDto.type = attachment.getType();
        taDto.description = attachment.getDescription();
        taDto.taskId = attachment.getTaskId();
        taDto.url = attachment.getUrl();
        taDto.createTime = attachment.getCreateTime();
        taDto.removalTime = attachment.getRemovalTime();
        taDto.rootProcessInstanceId = attachment.getRootProcessInstanceId();
        return taDto;
    }

}
