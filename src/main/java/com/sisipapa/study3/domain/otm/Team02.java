package com.sisipapa.study3.domain.otm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Team02 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team02_id")
    private Long id;
    private String name;

    @JsonIgnore
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name="team02_id")
    private List<Member02> members = new ArrayList<>();

    public void addMember(final Member02 member){
        members.add(member);
    }

    public Team02(String name){
        this.name = name;
    }

}
