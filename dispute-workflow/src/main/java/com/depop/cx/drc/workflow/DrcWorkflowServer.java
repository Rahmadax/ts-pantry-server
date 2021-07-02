package com.depop.cx.drc.workflow;

import org.camunda.bpm.engine.RuntimeService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.Resource;

@SpringBootApplication
public class DrcWorkflowServer {

    @Resource
    private RuntimeService runtimeService;

//    @Scheduled(fixedRate = 5000)
//    public void startProcess() {
//        runtimeService.startProcessInstanceByKey("hello_world_process");
//    }

    public static void main(final String[] args) {
        SpringApplication.run(DrcWorkflowServer.class, args);
    }

}
