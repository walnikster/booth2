package com.uleos.boot.booth2.counter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CounterRepository  extends JpaRepository<Counter, Long> {
}
