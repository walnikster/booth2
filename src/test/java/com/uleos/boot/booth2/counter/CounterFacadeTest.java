package com.uleos.boot.booth2.counter;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.util.AssertionErrors.assertNotNull;

public class CounterFacadeTest {

    @Mock
    private CounterRepository counterRepository;

    @InjectMocks
    private CounterFacade counterFacade;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testFindAllEmptyList() {
        when(counterRepository.findAll()).thenReturn(Collections.emptyList());
        List<Counter> allCounters = counterFacade.findAllCounters();
        assertThat(allCounters.size(), is(0));
        verify(counterRepository, times(1)).findAll();
    }


    @Test
    public void testFindAll() {
        when(counterRepository.findAll()).thenReturn(getTestList());
        List<Counter> allCounters = counterFacade.findAllCounters();
        assertThat(allCounters.size(), is(2));
        List<Long> ids = allCounters.stream().map(a -> a.getId()).collect(Collectors.toList());
        assertThat(ids, hasItems(-1L, -2L));
        verify(counterRepository, times(1)).findAll();
    }

    @Test
    public void testCreate() {
        when(counterRepository.count()).thenReturn(10L);
        when(counterRepository.save(any())).thenReturn(getSingleCounter());
        Counter saved = counterFacade.createCounterEntry("testuser1");
        ArgumentCaptor<Counter> arg = ArgumentCaptor.forClass(Counter.class);
        verify(counterRepository, times(1)).save(arg.capture());
        assertThat(arg.getValue().getCounter(), is(11L));
        assertThat(arg.getValue().getUser(), is("testuser1"));
        assertNotNull("not null", saved);
        assertNotNull("id not null", saved.getId());

    }

    private static Counter getSingleCounter() {
        Counter c1 = new Counter();
        c1.setUser("Test 1");
        c1.setCounter(1L);
        c1.setId(-1L);
        c1.setCreated(LocalDate.now());
        return c1;
    }


    private static List<Counter> getTestList() {
        List<Counter> testList = new ArrayList<>();
        Counter c1 = new Counter();
        c1.setUser("Test 1");
        c1.setCounter(1L);
        c1.setId(-1L);
        c1.setCreated(LocalDate.now());
        Counter c2 = new Counter();
        c2.setUser("Test 2");
        c2.setCounter(2L);
        c2.setId(-2L);
        c2.setCreated(LocalDate.now());
        return List.of(c2, c1);
    }


}