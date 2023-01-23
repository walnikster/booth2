package com.uleos.boot.booth2.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("counter")
public class CounterRestEndpoint {

    @Autowired
    private CounterFacade counterFacade;

    @GetMapping
    @PreAuthorize("hasRole('readwrite')")
    public List<Counter> getAll() {
        return this.counterFacade.findAllCounters();
    }

    @PostMapping
    public  Counter createCounter(@RequestBody  String user) {
        return this.counterFacade.createCounterEntry(user);
    }


}
