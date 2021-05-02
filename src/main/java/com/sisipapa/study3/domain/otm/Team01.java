package com.sisipapa.study3.domain.otm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Team01 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team01_id")
    private Long id;
    private String name;

    @JsonIgnore
    @OneToMany
    @JoinColumn(name = "team01_id")
    private List<Member01> members = new ArrayList<>();
}
