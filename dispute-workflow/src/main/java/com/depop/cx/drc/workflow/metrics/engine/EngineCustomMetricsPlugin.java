package com.depop.cx.drc.workflow.metrics.engine;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.event.EventType;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

/**
 * Camunda custom metrics plugin
 */
public class EngineCustomMetricsPlugin {

    private static final String ACTIVE_INCIDENTS_METRIC_NAME = "active.incidents";
    private static final String ACTIVE_USER_TASKS_METRIC_NAME = "active.user.tasks";
    private static final String ACTIVE_MESSAGE_EVENT_SUBSCRIPTIONS_METRIC_NAME = "active.message.event.subscriptions";
    private static final String ACTIVE_SIGNAL_EVENT_SUBSCRIPTIONS_METRIC_NAME = "active.signal.event.subscriptions";
    private static final String ACTIVE_COMPENSATE_EVENT_SUBSCRIPTIONS_METRIC_NAME = "active.compensate.event.subscriptions";
    private static final String ACTIVE_CONDITIONAL_EVENT_SUBSCRIPTIONS_METRIC_NAME = "active.conditional.event.subscriptions";
    private static final String EXECUTABLE_JOBS_METRIC_NAME = "executable.jobs";
    private static final String EXECUTABLE_TIMER_JOBS_METRIC_NAME = "executable.timer.jobs";
    private static final String TIMER_JOBS_METRIC_NAME = "timer.jobs";
    private static final String MESSAGE_JOBS_METRIC_NAME = "message.jobs";
    private static final String USER_COUNT_METRIC_NAME = "user.count";
    private static final String TENANT_COUNT_METRIC_NAME = "tenant.count";
    private static final String ACTIVE_PROCESS_INSTANCES_METRIC_NAME = "active.process.instances";
    private static final String COMPLETED_PROCESS_INSTANCES_METRIC_NAME = "completed.process.instances";
    private static final String ACTIVE_PROCESS_DEFINITIONS_METRIC_NAME = "active.process.definitions";
    private static final String DEPLOYMENTS_METRIC_NAME = "deployments";
    private static final String ACTIVE_EXTERNAL_TASKS_METRIC_NAME = "active.external.tasks";
    private static final String ACTIVE_LOCKED_EXTERNAL_TASKS_METRIC_NAME = "active.locked.external.tasks";
    private static final String ACTIVE_NOT_LOCKED_EXTERNAL_TASKS_METRIC_NAME = "active.not.locked.external.tasks";

    private final MeterRegistry micrometerRegistry;
    private final ProcessEngine processEngine;
    private final String prefix;
    private final Counter activeIncidents;
    private final Counter activeUserTasks;
    private final Counter activeMessageEventSubscriptions;
    private final Counter activeSignalEventSubscriptions;
    private final Counter activeCompensateEventSubscriptions;
    private final Counter activeConditionalEventSubscriptions;
    private final Counter executableJobs;
    private final Counter executableTimerJobs;
    private final Counter timerJobs;
    private final Counter messageJobs;
    private final Counter userCount;
    private final Counter tenantCount;
    private final Counter activeProcessInstances;
    private final Counter completedProcessInstances;
    private final Counter activeProcessDefinitions;
    private final Counter deployments;
    private final Counter activeExternalTasks;
    private final Counter activeLockedExternalTasks;
    private final Counter activeNotLockedExternalTasks;

    public EngineCustomMetricsPlugin(final MeterRegistry micrometerRegistry,
                                     final ProcessEngine processEngine,
                                     final String prefix) {
        this.micrometerRegistry = micrometerRegistry;
        this.processEngine = processEngine;
        this.prefix = prefix;

        final List<Tag> commonTags = List.of(Tag.of("engine_name", processEngine.getName()));
        activeIncidents = registerCounter(ACTIVE_INCIDENTS_METRIC_NAME, commonTags);
        activeUserTasks = registerCounter(ACTIVE_USER_TASKS_METRIC_NAME, commonTags);
        activeMessageEventSubscriptions = registerCounter(ACTIVE_MESSAGE_EVENT_SUBSCRIPTIONS_METRIC_NAME, commonTags);
        activeSignalEventSubscriptions = registerCounter(ACTIVE_SIGNAL_EVENT_SUBSCRIPTIONS_METRIC_NAME, commonTags);
        activeCompensateEventSubscriptions = registerCounter(ACTIVE_COMPENSATE_EVENT_SUBSCRIPTIONS_METRIC_NAME, commonTags);
        activeConditionalEventSubscriptions = registerCounter(ACTIVE_CONDITIONAL_EVENT_SUBSCRIPTIONS_METRIC_NAME, commonTags);
        executableJobs = registerCounter(EXECUTABLE_JOBS_METRIC_NAME, commonTags);
        executableTimerJobs = registerCounter(EXECUTABLE_TIMER_JOBS_METRIC_NAME, commonTags);
        timerJobs = registerCounter(TIMER_JOBS_METRIC_NAME, commonTags);
        messageJobs = registerCounter(MESSAGE_JOBS_METRIC_NAME, commonTags);
        userCount = registerCounter(USER_COUNT_METRIC_NAME, commonTags);
        tenantCount = registerCounter(TENANT_COUNT_METRIC_NAME, commonTags);
        activeProcessInstances = registerCounter(ACTIVE_PROCESS_INSTANCES_METRIC_NAME, commonTags);
        completedProcessInstances = registerCounter(COMPLETED_PROCESS_INSTANCES_METRIC_NAME, commonTags);
        activeProcessDefinitions = registerCounter(ACTIVE_PROCESS_DEFINITIONS_METRIC_NAME, commonTags);
        deployments = registerCounter(DEPLOYMENTS_METRIC_NAME, commonTags);
        activeExternalTasks = registerCounter(ACTIVE_EXTERNAL_TASKS_METRIC_NAME, commonTags);
        activeLockedExternalTasks = registerCounter(ACTIVE_LOCKED_EXTERNAL_TASKS_METRIC_NAME, commonTags);
        activeNotLockedExternalTasks = registerCounter(ACTIVE_NOT_LOCKED_EXTERNAL_TASKS_METRIC_NAME, commonTags);

    }

    private Counter registerCounter(final String name, final List<Tag> commonTags) {
        return micrometerRegistry.counter(prefix + name, commonTags);
    }


    @Scheduled(fixedRate = 60000L)
    void getActiveIncidents() {
        activeIncidents.increment(processEngine.getRuntimeService().createIncidentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveUserTasks() {
        activeUserTasks.increment(processEngine.getTaskService().createTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveMessageEventSubscriptions() {
        activeMessageEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.MESSAGE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveSignalEventSubscriptions() {
        activeSignalEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.SIGNAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveCompensateEventSubscriptions() {
        activeCompensateEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.COMPENSATE.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveConditionalEventSubscriptions() {
        activeConditionalEventSubscriptions.increment(processEngine.getRuntimeService().createEventSubscriptionQuery().eventType(EventType.CONDITONAL.name()).count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableJobs() {
        executableJobs.increment(processEngine.getManagementService().createJobQuery().executable().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getExecutableTimerJobs() {
        executableTimerJobs.increment(processEngine.getManagementService().createJobQuery().executable().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getTimerJobs() {
        timerJobs.increment(processEngine.getManagementService().createJobQuery().timers().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getMessageJobs() {
        messageJobs.increment(processEngine.getManagementService().createJobQuery().messages().count());
    }

    @Scheduled(fixedRate = 36000000L)
        // Once an hour
    void getUserCount() {
        userCount.increment(processEngine.getIdentityService().createUserQuery().count());
    }

    @Scheduled(fixedRate = 36000000L)
        // Once an hour
    void getTenantCount() {
        tenantCount.increment(processEngine.getIdentityService().createTenantQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessInstances() {
        activeProcessInstances.increment(processEngine.getRuntimeService().createProcessInstanceQuery().active().count());
    }

    @Scheduled(fixedRate = 36000000L)
        // Once an hour
    void getCompletedProcessInstances() {
        completedProcessInstances.increment(processEngine.getHistoryService().createHistoricProcessInstanceQuery().completed().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveProcessDefinitions() {
        activeProcessDefinitions.increment(processEngine.getRepositoryService().createProcessDefinitionQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getDeployments() {
        deployments.increment(processEngine.getRepositoryService().createDeploymentQuery().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveExternalTasks() {
        activeExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveLockedExternalTasks() {
        activeLockedExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().locked().count());
    }

    @Scheduled(fixedRate = 60000L)
    void getActiveNotLockedExternalTasks() {
        activeNotLockedExternalTasks.increment(processEngine.getExternalTaskService().createExternalTaskQuery().active().notLocked().count());
    }

}
