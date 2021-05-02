package com.sisipapa.study3.domain.otm;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Member01 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member01_id")
    private Long id;
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "team01_id")
    private Team01 team01;

}
