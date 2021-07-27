package com.depop.cx.drc.workflow.security.authentication;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Extracts the {@link DepopGrantedAuthority}s from scope attributes typically found in a {@link Jwt}.
 *
 * @author Tom Greasley
 */
public class DepopJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String[] CLAIM_SETS = new String[]{"legacy_roles", "mfa_roles", "int_svc_mfa_roles"};

    /**
     * Extract {@link DepopGrantedAuthority}s from the given {@link Jwt}.
     *
     * @param jwt The {@link Jwt} token
     * @return The {@link DepopGrantedAuthority authorities} read from the token scopes
     */
    @Override
    public Collection<GrantedAuthority> convert(@Nullable final Jwt jwt) {
        if (jwt == null) {
            return Collections.emptySet();
        }
        return Arrays.stream(CLAIM_SETS)
                .filter(jwt::hasClaim)
                .flatMap(claimSet ->
                        jwt.getClaimAsStringList(claimSet)
                                .stream()
                                .filter(Objects::nonNull)
                                .map(claim -> new DepopGrantedAuthority(claimSet, claim)))
                .collect(Collectors.toList());
    }

}