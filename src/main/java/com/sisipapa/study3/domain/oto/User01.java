package com.sisipapa.study3.domain.oto;

import lombok.*;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class User01 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user01_id")
    private Long id;
    private String name;

    @OneToOne
    @JoinColumn(name = "address01_id")
    private Address01 address;
}
