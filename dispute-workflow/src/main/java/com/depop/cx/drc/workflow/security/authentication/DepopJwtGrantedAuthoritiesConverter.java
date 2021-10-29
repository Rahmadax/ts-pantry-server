package com.depop.cx.drc.workflow.security.authentication;

import org.jetbrains.annotations.NotNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Extracts the {@link DepopGrantedAuthority}s from scope attributes typically found in a {@link Jwt}.
 *
 * @author Tom Greasley
 */
public class DepopJwtGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

    private static final String[] CLAIM_SETS = new String[]{"legacy_roles", "mfa_roles", "int_svc_mfa_roles"};
    private static final String ROLE_PREFIX = "ROLE_";
    private static final String OPSTOOLS_CLAIM = "opstools";

    private final JwtGrantedAuthoritiesConverter defaultAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    /**
     * Extract {@link DepopGrantedAuthority}s from the given {@link Jwt}.
     *
     * @param jwt The {@link Jwt} token
     * @return The {@link DepopGrantedAuthority authorities} ????read from the token scopes
     */
    @Override
    public Collection<GrantedAuthority> convert(@NotNull final Jwt jwt) {
        final var result = defaultAuthoritiesConverter.convert(jwt);
        result.addAll(Arrays.stream(CLAIM_SETS)
                .filter(jwt::hasClaim)
                .flatMap(claimSet ->
                        jwt.getClaimAsStringList(claimSet)
                                .stream()
                                .filter(Objects::nonNull)
                                .map(claim -> ROLE_PREFIX + claim.toUpperCase())
                                .map(claim -> new DepopGrantedAuthority(claimSet, claim)))
                .collect(Collectors.toSet()));

        if (jwt.hasClaim(OPSTOOLS_CLAIM) && (boolean) jwt.getClaim(OPSTOOLS_CLAIM)) {
            result.add(new DepopGrantedAuthority(OPSTOOLS_CLAIM, ROLE_PREFIX + OPSTOOLS_CLAIM));
        }

        return result;
    }

}