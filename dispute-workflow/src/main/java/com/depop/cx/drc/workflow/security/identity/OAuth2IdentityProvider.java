package com.depop.cx.drc.workflow.security.identity;

import org.camunda.bpm.engine.identity.*;
import org.camunda.bpm.engine.impl.GroupQueryImpl;
import org.camunda.bpm.engine.impl.Page;
import org.camunda.bpm.engine.impl.TenantQueryImpl;
import org.camunda.bpm.engine.impl.UserQueryImpl;
import org.camunda.bpm.engine.impl.identity.IdentityProviderException;
import org.camunda.bpm.engine.impl.identity.ReadOnlyIdentityProvider;
import org.camunda.bpm.engine.impl.identity.db.DbIdentityServiceProvider;
import org.camunda.bpm.engine.impl.interceptor.CommandContext;
import org.camunda.bpm.engine.impl.persistence.entity.GroupEntity;
import org.camunda.bpm.engine.impl.persistence.entity.TenantEntity;
import org.camunda.bpm.engine.impl.persistence.entity.UserEntity;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * OAuth2 identity provider with fallback for {@link DbIdentityServiceProvider}
 * if the Spring security context doesn't contain an authenticated user.
 * <p>
 * Since the fallback {@link DbIdentityServiceProvider} is a writeable provider
 * this class is also writeable but with OAuth2 authentication it works effectively as a read-only provider.
 */
public class OAuth2IdentityProvider implements ReadOnlyIdentityProvider {

    private static final String ROLE_PREFIX = "ROLE_";

    /**
     * @param searchLike the like value to search for
     * @param value      the actual user attribute value
     * @return true if either values are {@code null} or if {@code value} contains {@code searchLike} (case-insensitive)
     */
    protected static boolean nullOrContainsIgnoreCase(String searchLike, String value) {
        return searchLike == null || value == null || value.toLowerCase()
                .contains(searchLike.replaceAll("%", "").toLowerCase());
    }

    protected static UserEntity transformUser() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return null;
        }

        String userId = authentication.getName();
        UserEntity user = new UserEntity();
        user.setId(userId);
        user.setFirstName(userId);
        user.setLastName("");
        user.setEmail(userId);

        return user;
    }

    protected static List<Group> transformGroups() {
        return SecurityContextHolder.getContext()
                .getAuthentication()
                .getAuthorities()
                .stream()
                .filter(auth -> auth.getAuthority().startsWith(ROLE_PREFIX))
                .map(a -> {
            var group = new GroupEntity();
            var groupName = a.getAuthority().replaceFirst(ROLE_PREFIX, "");
            group.setId(groupName);
            group.setName(groupName);
            return group;
        }).collect(Collectors.toList());
    }

    public static class OAuth2UserQuery extends UserQueryImpl {
        @Override
        public long executeCount(CommandContext commandContext) {
            return 1;
        }

        @Override
        public List<User> executeList(CommandContext commandContext, Page page) {
            if (this.tenantId != null) {
                throw new IdentityProviderException("This filter is not supported for OAuth2 identity provider.");
            }

            return Stream.of(transformUser())
                    .filter(Objects::nonNull)
                    .filter(u -> this.id == null || this.id.equals(u.getId()))
                    .filter(u -> this.ids == null || Arrays.stream(this.ids).anyMatch(id -> u.getId().equals(id)))
                    .filter(u -> this.firstName == null || this.firstName.equals(u.getFirstName()))
                    .filter(u -> nullOrContainsIgnoreCase(this.firstNameLike, u.getFirstName()))
                    .filter(u -> this.lastName == null || this.lastName.equals(u.getLastName()))
                    .filter(u -> nullOrContainsIgnoreCase(this.lastNameLike, u.getLastName()))
                    .filter(u -> this.email == null || this.email.equals(u.getEmail()))
                    .filter(u -> nullOrContainsIgnoreCase(this.emailLike, u.getEmail()))
                    .filter(u -> this.groupId == null || transformGroups().stream().anyMatch(g -> g.getId().equals(this.groupId)))
                    .collect(Collectors.toList());
        }
    }

    @Override
    public UserEntity findUserById(String userId) {
        var user = transformUser();
        return user != null && Objects.equals(userId, user.getId()) ? user : null;
    }

    @Override
    public UserQuery createUserQuery() {
        return new OAuth2UserQuery();
    }

    @Override
    public UserQueryImpl createUserQuery(CommandContext commandContext) {
        return new OAuth2UserQuery();
    }

    @Override
    public NativeUserQuery createNativeUserQuery() {
        throw new IdentityProviderException("This filter is not supported for OAuth2 identity provider.");
    }

    @Override
    public boolean checkPassword(String userId, String password) {
        return false;
    }

    public static class OAuth2GroupQuery extends GroupQueryImpl {

        @Override
        public long executeCount(CommandContext commandContext) {
            return executeList(commandContext, null).size();
        }

        @Override
        public List<Group> executeList(CommandContext commandContext, Page page) {
            if (this.type != null || this.tenantId != null) {
                throw new IdentityProviderException("This filter is not supported for OAuth2 identity provider.");
            }

            return transformGroups().stream()
                    .filter(g -> this.id == null || this.id.equals(g.getId()))
                    .filter(g -> this.ids == null || Arrays.stream(this.ids).anyMatch(id -> g.getId().equals(id)))
                    .filter(g -> this.name == null || this.name.equals(g.getName()))
                    .filter(g -> nullOrContainsIgnoreCase(this.nameLike, g.getName()))
                    .filter(g -> {
                        var user = transformUser();
                        return this.userId == null || user == null || this.userId.equals(user.getId());
                    })
                    .collect(Collectors.toList());
        }
    }

    @Override
    public GroupEntity findGroupById(String groupId) {
        var groups = transformGroups();
        return (GroupEntity) groups.stream().filter(g -> g.getId().equals(groupId)).findFirst().orElse(null);
    }

    @Override
    public GroupQuery createGroupQuery() {
        return new OAuth2GroupQuery();
    }

    @Override
    public GroupQuery createGroupQuery(CommandContext commandContext) {
        return new OAuth2GroupQuery();
    }

    public static class OAuth2TenantQuery extends TenantQueryImpl {
        @Override
        public long executeCount(CommandContext commandContext) {
            return 0;
        }

        @Override
        public List<Tenant> executeList(CommandContext commandContext, Page page) {
            return Collections.emptyList();
        }
    }

    @Override
    public TenantEntity findTenantById(String tenantId) {
        return null;
    }

    @Override
    public TenantQuery createTenantQuery() {
        return new OAuth2TenantQuery();
    }

    @Override
    public TenantQuery createTenantQuery(CommandContext commandContext) {
        return new OAuth2TenantQuery();
    }

    @Override
    public void flush() {
        // no-op
    }

    @Override
    public void close() {
        // no-op
    }

}