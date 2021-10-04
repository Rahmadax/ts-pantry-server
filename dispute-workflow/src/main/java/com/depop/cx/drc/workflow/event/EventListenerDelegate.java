package com.depop.cx.drc.workflow.event;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.DelegateTask;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * <p>{@link ExecutionListener} and {@link TaskListener} implementation delegating to
 * a list of {@link ExecutionListener}s and {@link TaskListener}s provided in the
 * constructor.</p>
 */
public class EventListenerDelegate implements ExecutionListener, TaskListener {

    private static final Logger LOG = LoggerFactory.getLogger(EventListenerDelegate.class);

    private final List<TaskListener> taskListeners;
    private final List<ExecutionListener> executionListeners;

    public EventListenerDelegate(final List<TaskListener> taskListeners,
                                 final List<ExecutionListener> executionListeners) {
        this.taskListeners = taskListeners;
        this.executionListeners = executionListeners;
    }

    @Override
    public void notify(final DelegateExecution execution) {
        executionListeners.forEach(listener -> {
            try {
                listener.notify(execution);
            } catch (final Exception e) {
                LOG.error("Error invoking execution listener.", e);
            }
        });
    }

    @Override
    public void notify(final DelegateTask task) {
        taskListeners.forEach(listener -> {
            try {
                listener.notify(task);
            } catch (final Exception e) {
                LOG.error("Error invoking task listener.", e);
            }
        });
    }
}
