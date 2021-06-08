package com.depop.cx.drc.workflow;

import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HelloWorldTask implements JavaDelegate {

    private static final Logger log = LoggerFactory.getLogger(HelloWorldTask.class);

    @Override
    public void execute(final DelegateExecution execution) {
        log.info("Hello World!");
    }
}
