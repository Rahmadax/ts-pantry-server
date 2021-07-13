package com.depop.cx.drc.workflow.metrics.engine;

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
    private static final String GAUGE_PREFIX = "depop.service.dispute_workflow.camunda.engine.";

    public EngineCustomMetricsPlugin(final MeterRegistry micrometerRegistry, final ProcessEngine processEngine) {
        this.micrometerRegistry = micrometerRegistry;
        this.processEngine = processEngine;
    }

    List<Tag> commonTags;

    String activeIncidentsMetricName = GAUGE_PREFIX + "active.incidents";
    AtomicLong activeIncidents;

    String activeUserTasksMetricName = GAUGE_PREFIX + "active.user.tasks";
    AtomicLong activeUserTasks;

    String activeMessageEventSubscriptionsMetricName = GAUGE_PREFIX + "active.message.event.subscriptions";
    AtomicLong activeMessageEventSubscriptions;

    String activeSignalEventSubscriptionsMetricName = GAUGE_PREFIX + "active.signal.event.subscriptions";
    AtomicLong activeSignalEventSubscriptions;

    String activeCompensateEventSubscriptionsMetricName = GAUGE_PREFIX + "active.compensate.event.subscriptions";
    AtomicLong activeCompensateEventSubscriptions;

    String activeConditionalEventSubscriptionsMetricName = GAUGE_PREFIX + "active.conditional.event.subscriptions";
    AtomicLong activeConditionalEventSubscriptions;

    String executableJobsMetricName = GAUGE_PREFIX + "executable.jobs";
    AtomicLong executableJobs;

    String executableTimerJobsMetricName = GAUGE_PREFIX + "executable.timer.jobs";
    AtomicLong executableTimerJobs;

    String timerJobsMetricName = GAUGE_PREFIX + "timer.jobs";
    AtomicLong timerJobs;

    String messageJobsMetricName = GAUGE_PREFIX + "message.jobs";
    AtomicLong messageJobs;

    String userCountMetricName = GAUGE_PREFIX + "user.count";
    AtomicLong userCount;

    String tenantCountMetricName = GAUGE_PREFIX + "tenant.count";
    AtomicLong tenantCount;

    String activeProcessInstancesMetricName = GAUGE_PREFIX + "active.process.instances";
    AtomicLong activeProcessInstances;

    String completedProcessInstancesMetricName = GAUGE_PREFIX + "completed.process.instances";
    AtomicLong completedProcessInstances;

    String activeProcessDefinitionsMetricName = GAUGE_PREFIX + "active.process.definitions";
    AtomicLong activeProcessDefinitions;

    String deploymentsMetricName = GAUGE_PREFIX + "deployments";
    AtomicLong deployments;

    String activeExternalTasksMetricName = GAUGE_PREFIX + "active.external.tasks";
    AtomicLong activeExternalTasks;

    String activeLockedExternalTasksMetricName = GAUGE_PREFIX + "active.locked.external.tasks";
    AtomicLong activeLockedExternalTasks;

    String activeNotLockedExternalTasksMetricName = GAUGE_PREFIX + "active.not.locked.external.tasks";
    AtomicLong activeNotLockedExternalTasks;


    @PostConstruct
    void setup(){
        commonTags = List.of(Tag.of("engine_name", processEngine.getName()));
        activeIncidents = micrometerRegistry.gauge(activeIncidentsMetricName, commonTags, new AtomicLong(0));
        activeUserTasks = micrometerRegistry.gauge(activeUserTasksMetricName, commonTags, new AtomicLong(0));
        activeMessageEventSubscriptions = micrometerRegistry.gauge(activeMessageEventSubscriptionsMetricName, commonTags, new AtomicLong(0));
        activeSignalEventSubscriptions = micrometerRegistry.gauge(activeSignalEventSubscriptionsMetricName, commonTags, new AtomicLong(0));
        activeCompensateEventSubscriptions = micrometerRegistry.gauge(activeCompensateEventSubscriptionsMetricName, commonTags, new AtomicLong(0));
        activeConditionalEventSubscriptions = micrometerRegistry.gauge(activeConditionalEventSubscriptionsMetricName, commonTags, new AtomicLong(0));
        executableJobs = micrometerRegistry.gauge(executableJobsMetricName, commonTags, new AtomicLong(0));
        executableTimerJobs = micrometerRegistry.gauge(executableTimerJobsMetricName, commonTags, new AtomicLong(0));
        timerJobs = micrometerRegistry.gauge(timerJobsMetricName, commonTags, new AtomicLong(0));
        messageJobs = micrometerRegistry.gauge(messageJobsMetricName, commonTags, new AtomicLong(0));
        userCount = micrometerRegistry.gauge(userCountMetricName, commonTags, new AtomicLong(0));
        tenantCount = micrometerRegistry.gauge(tenantCountMetricName, commonTags, new AtomicLong(0));
        activeProcessInstances = micrometerRegistry.gauge(activeProcessInstancesMetricName, commonTags, new AtomicLong(0));
        completedProcessInstances = micrometerRegistry.gauge(completedProcessInstancesMetricName, commonTags, new AtomicLong(0));
        activeProcessDefinitions = micrometerRegistry.gauge(activeProcessDefinitionsMetricName, commonTags, new AtomicLong(0));
        deployments = micrometerRegistry.gauge(deploymentsMetricName, commonTags, new AtomicLong(0));
        activeExternalTasks = micrometerRegistry.gauge(activeExternalTasksMetricName, commonTags, new AtomicLong(0));
        activeLockedExternalTasks = micrometerRegistry.gauge(activeLockedExternalTasksMetricName, commonTags, new AtomicLong(0));
        activeNotLockedExternalTasks = micrometerRegistry.gauge(activeNotLockedExternalTasksMetricName, commonTags, new AtomicLong(0));
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveIncidents(){
        activeIncidents.set(processEngine.getRuntimeService().createIncidentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveUserTasks(){
        activeUserTasks.set(processEngine.getTaskService().createTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveMessageEventSubscriptions(){
        activeMessageEventSubscriptions.set(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.MESSAGE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveSignalEventSubscriptions(){
        activeSignalEventSubscriptions.set(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.SIGNAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveCompensateEventSubscriptions(){
        activeCompensateEventSubscriptions.set(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.COMPENSATE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveConditionalEventSubscriptions(){
        activeConditionalEventSubscriptions.set(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.CONDITONAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableJobs(){
        executableJobs.set(processEngine.getManagementService().createJobQuery().executable().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableTimerJobs(){
        executableTimerJobs.set(processEngine.getManagementService().createJobQuery().executable().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getTimerJobs(){
        timerJobs.set(processEngine.getManagementService().createJobQuery().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getMessageJobs(){
        messageJobs.set(processEngine.getManagementService().createJobQuery().messages().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getUserCount(){
        userCount.set(processEngine.getIdentityService().createUserQuery().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getTenantCount(){
        tenantCount.set(processEngine.getIdentityService().createTenantQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessInstances(){
        activeProcessInstances.set(processEngine.getRuntimeService().createProcessInstanceQuery().active().count());
    }

    @Scheduled(fixedRate = 36000000L) // Once an hour
    void getCompletedProcessInstances(){
        completedProcessInstances.set(processEngine.getHistoryService().createHistoricProcessInstanceQuery().completed().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessDefinitions(){
        activeProcessDefinitions.set(processEngine.getRepositoryService().createProcessDefinitionQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getDeployments(){
        deployments.set(processEngine.getRepositoryService().createDeploymentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveExternalTasks(){
        activeExternalTasks.set(processEngine.getExternalTaskService().createExternalTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveLockedExternalTasks(){
        activeLockedExternalTasks.set(processEngine.getExternalTaskService().createExternalTaskQuery().active().locked().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveNotLockedExternalTasks(){
        activeNotLockedExternalTasks.set(processEngine.getExternalTaskService().createExternalTaskQuery().active().notLocked().count());
    }

}
