package com.depop.cx.drc.workflow.config;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

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
                configuration.setStandaloneTasksEnabled(false);

                // Allow emails for usernames and underscore/dash in usergroup names.
                configuration.setUserResourceWhitelistPattern("[a-zA-Z0-9@_\\-.]+");
                configuration.setGroupResourceWhitelistPattern("[a-zA-Z0-9_-]+");
            }
        };
    }

}
