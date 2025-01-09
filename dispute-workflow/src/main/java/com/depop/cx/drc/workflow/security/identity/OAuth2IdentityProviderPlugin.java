package com.depop.cx.drc.workflow.security.identity;

import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.spring.boot.starter.util.SpringBootProcessEnginePlugin;

public class OAuth2IdentityProviderPlugin extends SpringBootProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        super.preInit(processEngineConfiguration);
        processEngineConfiguration.setIdentityProviderSessionFactory(new OAuth2IdentityProviderFactory());
    }
}