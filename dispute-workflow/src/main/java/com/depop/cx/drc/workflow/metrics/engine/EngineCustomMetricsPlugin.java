package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.event.EventType;
import org.camunda.bpm.engine.impl.metrics.reporter.DbMetricsReporter;
import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.AbstractCamundaConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultMetricsConfiguration;
import org.camunda.bpm.spring.boot.starter.property.MetricsProperty;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Camunda custom metrics plugin
 */
public class EngineCustomMetricsPlugin {

    private final MeterRegistry micrometerRegistry;
    private final ProcessEngine processEngine;
    private static final String PREFIX = "depop.service.dispute_workflow.camunda.engine.";

    public EngineCustomMetricsPlugin(final MeterRegistry micrometerRegistry, final ProcessEngine processEngine) {
        this.micrometerRegistry = micrometerRegistry;
        this.processEngine = processEngine;
    }

    List<Tag> commonTags;

    String activeIncidentsMetricName = PREFIX + "active.incidents";
    Counter activeIncidents;

    String activeUserTasksMetricName = PREFIX + "active.user.tasks";
    Counter activeUserTasks;

    String activeMessageEventSubscriptionsMetricName = PREFIX + "active.message.event.subscriptions";
    Counter activeMessageEventSubscriptions;

    String activeSignalEventSubscriptionsMetricName = PREFIX + "active.signal.event.subscriptions";
    Counter activeSignalEventSubscriptions;

    String activeCompensateEventSubscriptionsMetricName = PREFIX + "active.compensate.event.subscriptions";
    Counter activeCompensateEventSubscriptions;

    String activeConditionalEventSubscriptionsMetricName = PREFIX + "active.conditional.event.subscriptions";
    Counter activeConditionalEventSubscriptions;

    String executableJobsMetricName = PREFIX + "executable.jobs";
    Counter executableJobs;

    String executableTimerJobsMetricName = PREFIX + "executable.timer.jobs";
    Counter executableTimerJobs;

    String timerJobsMetricName = PREFIX + "timer.jobs";
    Counter timerJobs;

    String messageJobsMetricName = PREFIX + "message.jobs";
    Counter messageJobs;

    String userCountMetricName = PREFIX + "user.count";
    Counter userCount;

    String tenantCountMetricName = PREFIX + "tenant.count";
    Counter tenantCount;

    String activeProcessInstancesMetricName = PREFIX + "active.process.instances";
    Counter activeProcessInstances;

    String completedProcessInstancesMetricName = PREFIX + "completed.process.instances";
    Counter completedProcessInstances;

    String activeProcessDefinitionsMetricName = PREFIX + "active.process.definitions";
    Counter activeProcessDefinitions;

    String deploymentsMetricName = PREFIX + "deployments";
    Counter deployments;

    String activeExternalTasksMetricName = PREFIX + "active.external.tasks";
    Counter activeExternalTasks;

    String activeLockedExternalTasksMetricName = PREFIX + "active.locked.external.tasks";
    Counter activeLockedExternalTasks;

    String activeNotLockedExternalTasksMetricName = PREFIX + "active.not.locked.external.tasks";
    Counter activeNotLockedExternalTasks;


    @PostConstruct
    void setup(){
        commonTags = List.of(Tag.of("engine_name", processEngine.getName()));
        activeIncidents = micrometerRegistry.counter(activeIncidentsMetricName, commonTags);
        activeUserTasks = micrometerRegistry.counter(activeUserTasksMetricName, commonTags);
        activeMessageEventSubscriptions = micrometerRegistry.counter(activeMessageEventSubscriptionsMetricName, commonTags);
        activeSignalEventSubscriptions = micrometerRegistry.counter(activeSignalEventSubscriptionsMetricName, commonTags);
        activeCompensateEventSubscriptions = micrometerRegistry.counter(activeCompensateEventSubscriptionsMetricName, commonTags);
        activeConditionalEventSubscriptions = micrometerRegistry.counter(activeConditionalEventSubscriptionsMetricName, commonTags);
        executableJobs = micrometerRegistry.counter(executableJobsMetricName, commonTags);
        executableTimerJobs = micrometerRegistry.counter(executableTimerJobsMetricName, commonTags);
        timerJobs = micrometerRegistry.counter(timerJobsMetricName, commonTags);
        messageJobs = micrometerRegistry.counter(messageJobsMetricName, commonTags);
        userCount = micrometerRegistry.counter(userCountMetricName, commonTags);
        tenantCount = micrometerRegistry.counter(tenantCountMetricName, commonTags);
        activeProcessInstances = micrometerRegistry.counter(activeProcessInstancesMetricName, commonTags);
        completedProcessInstances = micrometerRegistry.counter(completedProcessInstancesMetricName, commonTags);
        activeProcessDefinitions = micrometerRegistry.counter(activeProcessDefinitionsMetricName, commonTags);
        deployments = micrometerRegistry.counter(deploymentsMetricName, commonTags);
        activeExternalTasks = micrometerRegistry.counter(activeExternalTasksMetricName, commonTags);
        activeLockedExternalTasks = micrometerRegistry.counter(activeLockedExternalTasksMetricName, commonTags);
        activeNotLockedExternalTasks = micrometerRegistry.counter(activeNotLockedExternalTasksMetricName, commonTags);
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveIncidents(){
        activeIncidents.increment(processEngine.getRuntimeService().createIncidentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveUserTasks(){
        activeUserTasks.increment(processEngine.getTaskService().createTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveMessageEventSubscriptions(){
        activeMessageEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.MESSAGE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveSignalEventSubscriptions(){
        activeSignalEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.SIGNAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveCompensateEventSubscriptions(){
        activeCompensateEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.COMPENSATE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveConditionalEventSubscriptions(){
        activeConditionalEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.CONDITONAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableJobs(){
        executableJobs.increment(processEngine.getManagementService().createJobQuery().executable().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableTimerJobs(){
        executableTimerJobs.increment(processEngine.getManagementService().createJobQuery().executable().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getTimerJobs(){
        timerJobs.increment(processEngine.getManagementService().createJobQuery().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getMessageJobs(){
        messageJobs.increment(processEngine.getManagementService().createJobQuery().messages().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getUserCount(){
        userCount.increment(processEngine.getIdentityService().createUserQuery().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getTenantCount(){
        tenantCount.increment(processEngine.getIdentityService().createTenantQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessInstances(){
        activeProcessInstances.increment(processEngine.getRuntimeService().createProcessInstanceQuery().active().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getCompletedProcessInstances(){
        completedProcessInstances.increment(processEngine.getHistoryService().createHistoricProcessInstanceQuery().completed().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessDefinitions(){
        activeProcessDefinitions.increment(processEngine.getRepositoryService().createProcessDefinitionQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getDeployments(){
        deployments.increment(processEngine.getRepositoryService().createDeploymentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveExternalTasks(){
        activeExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveLockedExternalTasks(){
        activeLockedExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().locked().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveNotLockedExternalTasks(){
        activeNotLockedExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().notLocked().count());
    }

}
