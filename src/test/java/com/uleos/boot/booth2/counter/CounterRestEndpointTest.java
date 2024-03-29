package com.uleos.boot.booth2.counter;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.verification.NoMoreInteractions;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;


class CounterRestEndpointTest {



    @Mock
    private CounterFacade counterFacade;

    @InjectMocks
    private CounterRestEndpoint counterRestEndpoint;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    public void testCreate() {
        counterRestEndpoint.createCounter("TestUser");
        verify(counterFacade, times(1)).createCounterEntry("TestUser");


    }

    @Test
    public void testFindAll() {

        when(counterRestEndpoint.getAll()).thenReturn(getTestList());
        List<Counter> all = counterRestEndpoint.getAll();
        assertThat(all.size(), CoreMatchers.is(2));
        assertThat(all.stream().map(Counter::getId).collect(Collectors.toList()), CoreMatchers.hasItems(-1L, -2L));
        verify(counterFacade, Mockito.times(1)).findAllCounters();

    }

    private static List<Counter> getTestList() {
        Counter c1 = new Counter();
        c1.setUsername("Test 1");
        c1.setCounter(1L);
        c1.setId(-1L);
        c1.setCreated(LocalDate.now());
        Counter c2 = new Counter();
        c2.setUsername("Test 2");
        c2.setCounter(2L);
        c2.setId(-2L);
        c2.setCreated(LocalDate.now());
        return List.of(c2, c1);
    }
}