package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.oto.User01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User01Repository extends JpaRepository<User01, Long> {
}
