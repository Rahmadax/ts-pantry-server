package com.depop.cx.drc.workflow.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.Assert;

/**
 * Depop specific concrete implementation of a {@link GrantedAuthority}.
 *
 * <p>
 * Stores a {@code String} representation of an authority granted to the
 * {@link org.springframework.security.core.Authentication Authentication} object.
 * <p>
 * Additionally stores a grant type e.g. legacy_roles, mfa_roles, int_svc_mfa_roles
 *
 * @author Tom Greasley
 */
public class DepopGrantedAuthority implements GrantedAuthority {

    private final String type;
    private final String role;

    public DepopGrantedAuthority(final String type, final String role) {
        Assert.hasText(type, "A granted authority textual type representation is required");
        Assert.hasText(role, "A granted authority textual representation is required");
        this.type = type;
        this.role = role;
    }

    @SuppressWarnings("unused")
    public String getType() {
        return this.type;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj instanceof DepopGrantedAuthority depopGrantedAuthority) {
            return this.type.equals(depopGrantedAuthority.type) &&
                    this.role.equals(depopGrantedAuthority.role);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }

    @Override
    public String toString() {
        return this.type + ":" + this.role;
    }

}
