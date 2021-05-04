package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.oto.User02;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface User02Repository extends JpaRepository<User02, Long> {
}
