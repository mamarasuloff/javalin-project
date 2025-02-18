package io.javalin.javalin_project;

import io.javalin.Javalin;
import io.javalin.apibuilder.ApiBuilder;
import io.javalin.javalin_project.config.Auth;
import io.javalin.javalin_project.controller.UserController;
import io.javalin.javalin_project.enums.Role;

public class JavalinProject {
	public static void main(String[] args) {
		Javalin app = Javalin.create(config -> {
			config.router.mount(router -> {
				router.beforeMatched(Auth::handleAccess);
			}).apiBuilder(() -> {
				ApiBuilder.get("/", ctx -> ctx.redirect("/users"), Role.ANYONE);
				ApiBuilder.path("users", () -> {
					ApiBuilder.get(UserController::getAllUserIds, Role.ANYONE);
					ApiBuilder.post(UserController::createUser, Role.USER_WRITE);
					ApiBuilder.path("{userId}", () -> {
						ApiBuilder.get(UserController::getUser, Role.USER_READ);
						ApiBuilder.patch(UserController::updateUser, Role.USER_WRITE);
						ApiBuilder.delete(UserController::deleteUser, Role.USER_WRITE);
					});
				});
			});
		}).start(7070);
	}
}
