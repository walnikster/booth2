package com.uleos.boot.booth2.counter;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.hasItems;

@DataJpaTest
class CounterRepositoryTest {


    @Autowired
    private CounterRepository counterRepository;

    @Test
    public void testCreate() {
        assertThat(counterRepository.count()).isZero();
        counterRepository.save(createCounter());
        assertThat(counterRepository.count()).isNotZero();
    }


    @Test
    @Sql("/insertsomecounter.sql")
    public void testFindAll() {
        List<Counter> all = counterRepository.findAll();
        assertThat(counterRepository.count()).isNotZero();
        assertThat(all.size()).isEqualTo(2);
        List<Long> ids = all.stream().map(Counter::getId).collect(Collectors.toList());
        MatcherAssert.assertThat(ids, hasItems(-20L, -10L));
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