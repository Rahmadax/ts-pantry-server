package com.depop.cx.drc.workflow.security.authentication;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

public class OktaOidcUserService extends OidcUserService {

    private static final String GROUPS_CLAIM_KEY = "groups";

    @Override
    public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
        final var user = super.loadUser(userRequest);

        // Only post process requests from the "Okta" reg
        if (!"okta".equals(userRequest.getClientRegistration().getRegistrationId())) {
            return user;
        }

        // start with authorities from super
        final Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());

        // add the groups as authorities
        authorities.addAll(groupsToAuthorities(user.getAttributes()));

        final var userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails().getUserInfoEndpoint().getUserNameAttributeName();

        return StringUtils.hasText(userNameAttributeName)
                ? new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo(), userNameAttributeName)
                : new DefaultOidcUser(authorities, user.getIdToken(), user.getUserInfo());
    }

    private Collection<? extends GrantedAuthority> groupsToAuthorities(final Map<String, Object> attributes) {
        if (!CollectionUtils.isEmpty(attributes) && StringUtils.hasText(GROUPS_CLAIM_KEY)) {
            final Object rawRoleClaim = attributes.get(GROUPS_CLAIM_KEY);
            if (rawRoleClaim instanceof Collection) {
                @SuppressWarnings("unchecked") final Collection<String> claims = (Collection<String>) rawRoleClaim;
                return claims.stream()
                        .filter(Objects::nonNull)
                        .map(claim -> "ROLE_" + claim.toUpperCase())
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());
            }
        }
        return Collections.emptySet();
    }

}
