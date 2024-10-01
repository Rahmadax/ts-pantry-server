package com.depop.cx.drc.workflow.database;

import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.impl.cfg.ProcessEngineConfigurationImpl;
import org.camunda.bpm.engine.impl.cfg.ProcessEnginePlugin;
import org.camunda.bpm.engine.impl.db.sql.DbSqlSessionFactory;
import org.camunda.bpm.spring.boot.starter.configuration.Ordering;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * <p>{@link ProcessEnginePlugin} implementation that adds support for h2 ver >2.1
 * Note: h2 is only used to run locally or for testing.
 */
@Component
@Order(Ordering.DEFAULT_ORDER + 2)
public class H2SqlConfigurationPlugin implements ProcessEnginePlugin {

    @Override
    public void preInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        DbSqlSessionFactory.databaseSpecificTrueConstant.put("h2", "true");
        DbSqlSessionFactory.databaseSpecificFalseConstant.put("h2", "false");
        DbSqlSessionFactory.databaseSpecificBitAnd2.put("h2", ",CAST(");
        DbSqlSessionFactory.databaseSpecificBitAnd3.put("h2", " AS BIGINT))");
    }

    @Override
    public void postInit(ProcessEngineConfigurationImpl processEngineConfiguration) {
        // in this case only preInit is necessary
    }

    @Override
    public void postProcessEngineBuild(ProcessEngine processEngine) {
        // in this case only preInit is necessary
    }
}
