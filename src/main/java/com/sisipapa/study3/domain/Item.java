package com.sisipapa.study3.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name = "dtype")
@Getter
@Setter
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_id")
    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String dtype;            // A(Album),B(Book),M(Movie)
    private String author;
    private String isbn;
    private String artist;
    private String etc;
    private String director;
    private String actor;
    @OneToMany(mappedBy = "item")
    private List<CategoryItem> categories = new ArrayList<CategoryItem>();

    public void removeStock(int count) {
        this.stockQuantity = this.stockQuantity - count;
    }
}
