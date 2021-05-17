package com.depop.cx.drc.workflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class DrcWorkflowServer {

    public static void main(final String[] args) {
        SpringApplication.run(DrcWorkflowServer.class, args);
    }

}
