package com.uleos.boot.booth2.counter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping("counter")
public class CounterRestEndpoint {

    @Autowired
    private CounterFacade counterFacade;

    @GetMapping
    public List<Counter> getAll() {
        return this.counterFacade.findAllCounters();
    }

    @PostMapping
    public  void createCounter(@RequestBody  String user) {
        this.counterFacade.createCounterEntry(user);
    }


}
