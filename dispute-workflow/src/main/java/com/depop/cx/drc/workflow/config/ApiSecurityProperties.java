package com.depop.cx.drc.workflow.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.UUID;

/**
 * Configuration properties for Api security
 *
 * @author Tom Greasley
 * @since 2024-09-26
 */
@ConfigurationProperties("api.security")
public class ApiSecurityProperties {

    /**
     * The username to use for basic auth authentication.
     **/
    private String username = "drc_admin";

    /**
     * The password to use for basic auth authentication.
     **/
    private String password =  UUID.randomUUID().toString();

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
