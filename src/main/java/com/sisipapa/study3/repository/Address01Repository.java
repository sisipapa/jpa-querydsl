package com.sisipapa.study3.repository;

import com.sisipapa.study3.domain.oto.Address01;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface Address01Repository extends JpaRepository<Address01, Long> {
}
