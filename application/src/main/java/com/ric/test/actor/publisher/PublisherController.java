package com.ric.test.actor.publisher;

import com.ric.test.model.Work;
import com.ric.test.service.PublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Profile("publisher")
public class PublisherController {

    @Autowired
    private PublisherService service;

    @RequestMapping(value = "/output/data", method = RequestMethod.GET)
    public List<Work> getWorkList() {
        return service.getWorkList();
    }
}
