package com.sisipapa.study3.entity;

import lombok.*;

import javax.persistence.*;

@Builder
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "team")
@Entity
@Table(name = "member1")
public class Member1 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public String name;

    public int age;

    @ManyToOne(fetch = FetchType.LAZY)
    public Team team;

    public Member1(String name, int age, Team team) {
        this.name = name;
        this.age = age;
        this.team = team;
    }
}
