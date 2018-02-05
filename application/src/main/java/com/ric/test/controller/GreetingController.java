package com.ric.test.controller;

import java.util.concurrent.atomic.AtomicLong;

import com.ric.test.model.Greeting;
import com.ric.test.model.Work;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;

@RestController
@Profile("greeting")
public class GreetingController {

    @Value("${spring.profiles.active}")
    private String actorProfile;

    private static final String template =  "Hello, %s! -> Role: %s";
    private final AtomicLong counter = new AtomicLong();


    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {
        System.out.println("PARAMETER -> Actor Profile : " + actorProfile );
        return new Greeting(counter.incrementAndGet(),
                String.format(template, name, actorProfile));
    }

    @RequestMapping(value="/post")
    public Work post(@RequestBody Work work) {
        work.setDescription("Return edited");
        return work;
    }
    /*
    @RequestMapping(value="/post", method= RequestMethod.POST,consumes =
            {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces={MediaType.APPLICATION_JSON_VALUE,MediaType.APPLICATION_JSON_VALUE})
    public Greeting post(@RequestBody Greeting name) {
        return new Greeting();
    }
    */

}
