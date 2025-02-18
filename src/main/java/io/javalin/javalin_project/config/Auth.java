package io.javalin.javalin_project.config;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import io.javalin.http.Context;
import io.javalin.http.Header;
import io.javalin.http.UnauthorizedResponse;
import io.javalin.javalin_project.enums.Role;
import io.javalin.security.RouteRole;

public class Auth {
	public static void handleAccess(Context ctx) {
        var permittedRoles = ctx.routeRoles();
        if (permittedRoles.contains(Role.ANYONE)) {
            return; // anyone can access
        }
        if (userRoles(ctx).stream().anyMatch(permittedRoles::contains)) {
            return; // user has role required to access
        }
        ctx.header(Header.WWW_AUTHENTICATE, "Basic");
        throw new UnauthorizedResponse();
    }

    public static List<Role> userRoles(Context ctx) {
        return Optional.ofNullable(ctx.basicAuthCredentials())
            .map(credentials -> userRolesMap.getOrDefault(new Pair(credentials.getUsername(), credentials.getPassword()), List.of()))
            .orElse(List.of());
    }

    record Pair(String a, String b) {}
    private static final Map<Pair, List<Role>> userRolesMap = Map.of(
        new Pair("alice", "weak-1234"), List.of(Role.USER_READ),
        new Pair("bob", "weak-123456"), List.of(Role.USER_READ, Role.USER_WRITE)
    );
}
