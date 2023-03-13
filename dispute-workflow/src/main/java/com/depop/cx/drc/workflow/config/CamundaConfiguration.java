package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.event.GlobalListenerPlugin;
import com.depop.cx.drc.workflow.listener.CamundaExtensionsVariableBuilder;
import org.camunda.bpm.engine.delegate.ExecutionListener;
import org.camunda.bpm.engine.delegate.TaskListener;
import org.camunda.bpm.engine.impl.cfg.AbstractProcessEnginePlugin;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import java.util.List;

@Configuration
public class CamundaConfiguration {

    @Bean
    @Order(Ordering.DEFAULT_ORDER + 1)
    public CamundaProcessEngineConfiguration drcProcessEngineConfiguration() {
        return new CamundaProcessEngineConfiguration() {
            @Override
            public void preInit(ProcessEngineConfigurationImpl configuration) {

                // Disable unused features that have an impact on DB performance.
                // https://docs.camunda.org/manual/7.15/user-guide/process-engine/database/performance/#disabling-cmmn-and-standalone-tasks
                configuration.setCmmnEnabled(false);
                configuration.setStandaloneTasksEnabled(true);

                // Allow emails for usernames and underscore/dash in usergroup names.
                configuration.setUserResourceWhitelistPattern("[a-zA-Z0-9@_\\-.]+");
                configuration.setGroupResourceWhitelistPattern("[a-zA-Z0-9_-]+");
            }
        };
    }

    /***
     * A plugin that will register TaskListener and ExecutionListener beans as global event listeners in camunda.
     *
     * @param executionListeners the {@link ExecutionListener}s to register.
     * @param taskListeners the {@link TaskListener}s to register.
     * @return an instance of the {@link GlobalListenerPlugin}.
     */
    @Bean
    public AbstractProcessEnginePlugin registerGlobalListeners(final List<ExecutionListener> executionListeners,
                                                               final List<TaskListener> taskListeners) {
        return new GlobalListenerPlugin(executionListeners, taskListeners);
    }

    /***
     * A global task listener that copies camunda specific properties from elements in the process definition
     * to local variables in the process instance.
     *
     * @return an instance of a {@link CamundaExtensionsVariableBuilder}.
     */
    @Bean
    public TaskListener camundaExtensionsVariableBuilder() {
        return new CamundaExtensionsVariableBuilder();
    }

}
