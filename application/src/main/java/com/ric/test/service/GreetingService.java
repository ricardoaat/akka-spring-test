package com.ric.test.service;

import org.springframework.stereotype.Component;

@Component
public class GreetingService {

    public String greet(String name) {
        System.out.println("Service of greeting (Actors 1 & 2)");
        return "Bonjour " + name;
    }

}
