package com.ric.test.router;

import akka.http.javadsl.server.Route;

public interface  Router {
    public Route createRoute();
}
