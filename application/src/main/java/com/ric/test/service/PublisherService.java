package com.ric.test.service;

import com.ric.test.model.Work;

import java.util.List;

public interface PublisherService {

    List<Work> getWorkList();
    void addNewWork(Work work);
    void cleanWorkList();

}
