package com.ric.test.service;

import org.springframework.stereotype.Service;

/**
 * Stateless service which calculates dummy CPU-intensive task
 */
@Service
public class ConsumerServiceImpl implements ConsumerService {

    private Integer seq = 0;

    @Override
    public Integer getWorkId() {
        return seq++;
    }
}
