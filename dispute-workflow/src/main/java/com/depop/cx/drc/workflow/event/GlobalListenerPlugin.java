package com.depop.cx.drc.workflow.event;

import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.bpmn.parser.BpmnParseListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>{@link org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin} enabling global event listener support.</p>
 */
public class GlobalListenerPlugin extends AbstractProcessEnginePlugin {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalListenerPlugin.class);

    private final BpmnParseListener globalParseListener;

    public GlobalListenerPlugin(final List<ExecutionListener> executionListeners,
                                final List<TaskListener> taskListeners) {

        executionListeners.forEach(executionListener
                -> LOG.info("Registering global execution listener {}.", executionListener.getClass().getSimpleName()));

        taskListeners.forEach(executionListener
                -> LOG.info("Registering global task listener {}.", executionListener.getClass().getSimpleName()));

        final var delegate = new EventListenerDelegate(taskListeners, executionListeners);
        globalParseListener = new GlobalEventParseListener(delegate, delegate);
    }

    @Override
    public void preInit(final ProcessEngineConfigurationImpl processEngineConfiguration) {
        var preParseListeners = processEngineConfiguration.getCustomPreBPMNParseListeners();
        if (preParseListeners == null) {
            preParseListeners = new ArrayList<>();
            processEngineConfiguration.setCustomPreBPMNParseListeners(preParseListeners);
        }
        preParseListeners.add(globalParseListener);
    }

}
