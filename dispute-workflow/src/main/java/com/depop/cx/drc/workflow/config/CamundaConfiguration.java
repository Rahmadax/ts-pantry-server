package com.depop.cx.drc.workflow.config;


import org.camunda.bpm.engine.spring.SpringProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.CamundaProcessEngineConfiguration;
import org.camunda.bpm.spring.boot.starter.configuration.impl.DefaultProcessEngineConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class CamundaConfiguration {

    @Bean
    @Primary
    @SuppressWarnings("java:S110")
    public CamundaProcessEngineConfiguration camundaProcessEngineConfiguration() {
        return new DefaultProcessEngineConfiguration() {
            @Override
            public void preInit(SpringProcessEngineConfiguration configuration) {
                super.preInit(configuration);

                // Disable unused features that have an impact on DB performance.
                // https://docs.camunda.org/manual/7.15/user-guide/process-engine/database/performance/#disabling-cmmn-and-standalone-tasks
                configuration.setCmmnEnabled(false);
                configuration.setStandaloneTasksEnabled(false);
            }
        };
    }

}
