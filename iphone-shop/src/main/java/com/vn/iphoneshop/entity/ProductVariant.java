package com.vn.iphoneshop.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = "product_variants")
public class ProductVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "variant_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "color", length = 50)
    private String color;

    @Column(name = "storage", length = 50)
    private String storage;

    @Column(name = "price", nullable = false, precision = 15, scale = 2)
    private BigDecimal price;

    @OneToMany(mappedBy = "variant")
    private Set<Device> devices = new LinkedHashSet<>();

    @OneToMany(mappedBy = "variant")
    private Set<OrderItem> orderItems = new LinkedHashSet<>();

}