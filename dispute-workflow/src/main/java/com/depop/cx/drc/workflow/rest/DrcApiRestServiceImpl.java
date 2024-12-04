package com.depop.cx.drc.workflow.rest;

import org.camunda.bpm.engine.rest.*;
import org.camunda.bpm.engine.rest.dto.task.AttachmentDto;
import org.camunda.bpm.engine.rest.history.HistoryRestService;
import org.camunda.bpm.engine.rest.impl.AbstractProcessEngineRestServiceImpl;
import org.camunda.bpm.engine.rest.impl.FetchAndLockRestServiceImpl;
import org.camunda.bpm.engine.rest.mapper.MultipartFormData;
import org.camunda.commons.utils.IoUtil;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@Path("")
public class DrcApiRestServiceImpl extends AbstractProcessEngineRestServiceImpl {

    /*
     * The following resources are used by dispute-service:
     *
     *  - GET /process-definition/key/${key}
     *  - GET /process-definition/${id}
     *  - POST /process-definition/key/${key}/start
     *
     *  - GET /process-instance/${processId}/variables/${varName}
     *  - GET /process-instance/${processId}/variables
     *
     *  - GET /variable-instance
     *
     *  - GET /task?processInstanceIdIn=${processId}&assignee=${assignee.toAssigneeIdentifier}
     *  - GET /task/${taskId}/localVariables
     *  - POST /task/${taskId}/complete
     *
     *  - GET /history/variable-instance
     *  - GET /history/process-instance/${processId}
     *  - GET /history/task?processInstanceId=${processId}&taskAssignee=${taskAssignee}&finished=true
     *  - GET /history/task?processInstanceId=${processId}&assigned=true
     *
     *  - POST /message
     *
     * The following resources are used by dispute-processing-service:
     *
     * - POST /external-task/fetchAndLock
     * - POST /external-task/${completeRequestWithTaskId.taskId}/complete
     * - POST /external-task/${errorMeta.taskId}/failure
     * - POST /message
     *
     * The modeller tool uses /deployment
     *
     * We only need to expose the following resources via the API:
     *  - process-definition
     *  - process-instance
     *  - variable-instance
     *  - task
     *  - history
     *  - message
     *  - external-task
     *  - external-task/fetchAndLock
     *  - deployment
     */

    public DrcApiRestServiceImpl() {
    }

    @POST
    @Path("/task/{taskId}/attachment/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AttachmentDto addAttachment(@PathParam("taskId") String taskId, @Context UriInfo uriInfo, TextAttachmentDto attachment) {

        var taskService = super.getTaskRestService(null);
        var task = taskService.getTask(taskId);
        var attachments = task.getAttachmentResource();

        var formData = new MultipartFormData();

        if (attachment.getName() != null) {
            var part = new WritableFormPart("attachment-name", MediaType.TEXT_PLAIN, attachment.getName());
            formData.addPart(part);
        }

        if (attachment.getType() != null) {
            var part = new WritableFormPart("attachment-type", MediaType.TEXT_PLAIN, attachment.getType());
            formData.addPart(part);
        }

        if (attachment.getDescription() != null) {
            var part = new WritableFormPart("attachment-description", MediaType.TEXT_PLAIN, attachment.getDescription());
            formData.addPart(part);
        }

        if (attachment.getContent() != null) {
            var part = new WritableFormPart("content", MediaType.TEXT_PLAIN, attachment.getContent());
            formData.addPart(part);
        }

        return attachments.addAttachment(uriInfo, formData);
    }

    @GET
    @Path("/task/{taskId}/attachment/{attachmentId}")
    @Produces(MediaType.APPLICATION_JSON)
    public TextAttachmentDto getAttachmentData(@PathParam("taskId") String taskId, @PathParam("attachmentId") String attachmentId) {
        var taskService = super.getTaskRestService(null);
        var task = taskService.getTask(taskId);

        var attachments = task.getAttachmentResource();
        var attachment = attachments.getAttachment(attachmentId);

        var attachmentData = attachments.getAttachmentData(attachmentId);

        var attachmentString = IoUtil.inputStreamAsString(attachmentData);

        var textAttachmentDto = TextAttachmentDto.fromAttachment(attachment);
        textAttachmentDto.setContent(attachmentString);

        return textAttachmentDto;
    }

    @GET
    @Path("/task/{taskId}/attachment")
    @Produces(MediaType.APPLICATION_JSON)
    public List<AttachmentDto> getAttachments(@PathParam("taskId") String taskId) {
        var taskService = super.getTaskRestService(null);
        var task = taskService.getTask(taskId);

        var attachmentsResource = task.getAttachmentResource();
        var attachments = attachmentsResource.getAttachments();

        return attachments.stream().map(attachmentDto -> {
                    var attachmentData = attachmentsResource.getAttachmentData(attachmentDto.getId());

                    var attachmentString = IoUtil.inputStreamAsString(attachmentData);

                    var textAttachmentDto = TextAttachmentDto.fromAttachment(attachmentDto);
                    textAttachmentDto.setContent(attachmentString);

                    return textAttachmentDto;
                }
        ).collect(Collectors.toList());
    }

    @Path("/process-definition")
    public ProcessDefinitionRestService getProcessDefinitionService() {
        return super.getProcessDefinitionService(null);
    }

    @Path("/process-instance")
    public ProcessInstanceRestService getProcessInstanceService() {
        return super.getProcessInstanceService(null);
    }

    @Path("/variable-instance")
    public VariableInstanceRestService getVariableInstanceService() {
        return super.getVariableInstanceService(null);
    }

    @Path("/task")
    public TaskRestService getTaskRestService() {
        return super.getTaskRestService(null);
    }

    @Path("/history")
    public HistoryRestService getHistoryRestService() {
        return super.getHistoryRestService(null);
    }

    @Path("/message")
    public MessageRestService getMessageRestService() {
        return super.getMessageRestService(null);
    }

    @Path("/external-task")
    public ExternalTaskRestService getExternalTaskRestService() {
        return super.getExternalTaskRestService(null);
    }

    @Path("/deployment")
    public DeploymentRestService getDeploymentRestService() {
        return super.getDeploymentRestService(null);
    }

    @Path("/external-task/fetchAndLock")
    public FetchAndLockRestService fetchAndLock() {
        String rootResourcePath = this.getRelativeEngineUri(null).toASCIIString();
        FetchAndLockRestServiceImpl subResource = new FetchAndLockRestServiceImpl(null, this.getObjectMapper());
        subResource.setRelativeRootResourceUri(rootResourcePath);
        return subResource;
    }

    /* THE FOLLOWING REST RESOURCES ARE NOT USED BY THE DRC SO WE CAN SAFELY REMOVE THEM FROM THE API: */

//    @Path("/execution")
//    public ExecutionRestService getExecutionService() {
//        return super.getExecutionService(null);
//    }
//
//    @Path("/identity")
//    public IdentityRestService getIdentityRestService() {
//        return super.getIdentityRestService(null);
//    }
//
//    @Path("/job-definition")
//    public JobDefinitionRestService getJobDefinitionRestService() {
//        return super.getJobDefinitionRestService(null);
//    }
//
//    @Path("/job")
//    public JobRestService getJobRestService() {
//        return super.getJobRestService(null);
//    }
//
//    @Path("/group")
//    public GroupRestService getGroupRestService() {
//        return super.getGroupRestService(null);
//    }
//
//    @Path("/user")
//    public UserRestService getUserRestService() {
//        return super.getUserRestService(null);
//    }
//
//    @Path("/authorization")
//    public AuthorizationRestService getAuthorizationRestService() {
//        return super.getAuthorizationRestService(null);
//    }
//
//    @Path("/incident")
//    public IncidentRestService getIncidentService() {
//        return super.getIncidentService(null);
//    }
//
//
//    @Path("/case-definition")
//    public CaseDefinitionRestService getCaseDefinitionRestService() {
//        return super.getCaseDefinitionRestService(null);
//    }
//
//    @Path("/case-instance")
//    public CaseInstanceRestService getCaseInstanceRestService() {
//        return super.getCaseInstanceRestService(null);
//    }
//
//    @Path("/case-execution")
//    public CaseExecutionRestService getCaseExecutionRestService() {
//        return super.getCaseExecutionRestService(null);
//    }
//
//    @Path("/filter")
//    public FilterRestService getFilterRestService() {
//        return super.getFilterRestService(null);
//    }
//
//    @Path("/metrics")
//    public MetricsRestService getMetricsRestService() {
//        return super.getMetricsRestService(null);
//    }
//
//    @Path("/decision-definition")
//    public DecisionDefinitionRestService getDecisionDefinitionRestService() {
//        return super.getDecisionDefinitionRestService(null);
//    }
//
//    @Path("/decision-requirements-definition")
//    public DecisionRequirementsDefinitionRestService getDecisionRequirementsDefinitionRestService() {
//        return super.getDecisionRequirementsDefinitionRestService(null);
//    }
//
//    @Path("/migration")
//    public MigrationRestService getMigrationRestService() {
//        return super.getMigrationRestService(null);
//    }
//
//    @Path("/modification")
//    public ModificationRestService getModificationRestService() {
//        return super.getModificationRestService(null);
//    }
//
//    @Path("/batch")
//    public BatchRestService getBatchRestService() {
//        return super.getBatchRestService(null);
//    }
//
//    @Path("/tenant")
//    public TenantRestService getTenantRestService() {
//        return super.getTenantRestService(null);
//    }
//
//    @Path("/signal")
//    public SignalRestService getSignalRestService() {
//        return super.getSignalRestService(null);
//    }
//
//    @Path("/condition")
//    public ConditionRestService getConditionRestService() {
//        return super.getConditionRestService(null);
//    }
//
//    @Path("/optimize")
//    public OptimizeRestService getOptimizeRestService() {
//        return super.getOptimizeRestService(null);
//    }
//
//    @Path("/version")
//    public VersionRestService getVersionRestService() {
//        return super.getVersionRestService(null);
//    }
//
//    @Path("/schema/log")
//    public SchemaLogRestService getSchemaLogRestService() {
//        return super.getSchemaLogRestService(null);
//    }
//
//    @Path("/event-subscription")
//    public EventSubscriptionRestService getEventSubscriptionRestService() {
//        return super.getEventSubscriptionRestService(null);
//    }
//
//    @Path("/telemetry")
//    public TelemetryRestService getTelemetryRestService() {
//        return super.getTelemetryRestService(null);
//    }

    protected URI getRelativeEngineUri(String engineName) {
        return URI.create("/");
    }

}
