package com.depop.cx.drc.workflow;

import org.camunda.bpm.engine.RuntimeService;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.spring.boot.starter.test.helper.AbstractProcessEngineRuleTest;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@Ignore("Some configuration work is required before tests work.")
public class WorkflowTest extends AbstractProcessEngineRuleTest {

    @Autowired
    public RuntimeService runtimeService;

    @Test
    public void shouldExecuteHappyPath() {
        // given
        String processDefinitionKey = "hello_world_process";

        // when
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey);

        Assert.assertTrue(true);

    }

}
