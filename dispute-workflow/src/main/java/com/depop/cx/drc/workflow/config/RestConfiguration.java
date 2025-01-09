package com.depop.cx.drc.workflow.config;

import com.depop.cx.drc.workflow.rest.DrcApiRestServiceImpl;
import jakarta.ws.rs.ApplicationPath;
import org.camunda.bpm.engine.rest.impl.CamundaRestResources;
import org.camunda.bpm.spring.boot.starter.rest.CamundaJerseyResourceConfig;
import org.glassfish.jersey.jackson.JacksonFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/***
 * An overriding instance of {@link CamundaJerseyResourceConfig}, providing a customised, restricted set of
 * Rest API resources - basically removing the API modules we don't use in the DRC.  See {@link DrcApiRestServiceImpl}
 * for the actual API definition.
 */
@Component
@Primary
@ApplicationPath("/engine-rest")
public class RestConfiguration extends CamundaJerseyResourceConfig {

    private static final Logger log = LoggerFactory.getLogger(RestConfiguration.class);

    protected void registerCamundaRestResources() {
        log.info("Configuring camunda rest api.");
        this.registerClasses(DrcApiRestServiceImpl.class);
        this.registerClasses(CamundaRestResources.getConfigurationClasses());
        this.register(JacksonFeature.class);
        log.info("Finished configuring camunda rest api.");
    }

}
