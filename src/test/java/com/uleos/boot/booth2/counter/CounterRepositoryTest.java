package com.uleos.boot.booth2.counter;

import org.assertj.core.api.Assertions;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@DataJpaTest
class CounterRepositoryTest {


    @Autowired
    CounterRepository counterRepository;

    @Test
    public void testCreate() {


        Assertions.assertThat(counterRepository.count()).isZero();
        counterRepository.save(createCounter());
        Assertions.assertThat(counterRepository.count()).isNotZero();


    }


    @Test
    @Sql("/insertsomecounter.sql")
    public void testFindAll() {


        List<Counter> all = counterRepository.findAll();
        Assertions.assertThat(counterRepository.count()).isNotZero();

        Assertions.assertThat(all.size()).isEqualTo(2);
        List<Long> ids = all.stream().map(Counter::getId).collect(Collectors.toList());

        MatcherAssert.assertThat(ids, CoreMatchers.hasItems(-20L, -10L));
    }

    private static Counter createCounter() {
        Counter c = new Counter();
        c.setId(null);
        c.setCreated(LocalDate.now());
        c.setUser("testuser");
        c.setCounter(10L);
        return c;
    }

}