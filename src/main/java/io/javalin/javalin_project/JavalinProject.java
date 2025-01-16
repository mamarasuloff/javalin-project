package io.javalin.javalin_project;

import io.javalin.Javalin;

public class JavalinProject {
    public static void main(String[] args) {
		var app = Javalin.create(/*config*/)
	            .get("/", ctx -> ctx.result("Javaline Test"))
	            .start(8090);
    }
}
