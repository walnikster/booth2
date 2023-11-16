package com.uleos.boot.booth2.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class CounterFacade {

    @Autowired
    private CounterRepository counterRepository;

    public List<Counter> findAllCounters() {
        return counterRepository.findAll();
    }


    public Counter createCounterEntry(String user) {
        Counter counter = new Counter();
        long actualCount = counterRepository.count();
        counter.setCounter(++actualCount);
        counter.setCreated(LocalDate.now());
        counter.setUsername(user);
        String cred = "npm_access_token";
        String pswd = "kkkk_aaaa_123";
        return counterRepository.save(counter);

    }


}
