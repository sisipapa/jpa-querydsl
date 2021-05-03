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
public class Team03 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "team03_id")
    private Long id;
    private String name;

    @JsonIgnore
    @OneToMany(mappedBy = "team03", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Member03> members = new ArrayList<>();

    public void addMember(final Member03 member){
        members.add(member);
    }

    public Team03(String name){
        this.name = name;
    }

}
