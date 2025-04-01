package org.example.yourstockv2backend.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.example.yourstockv2backend.model.enums.OrderStatus;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "\"order\"")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @ColumnDefault("nextval('order_id_seq')")
    @Column(name = "id", nullable = false)
    private Long id;

    @ColumnDefault("CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @NotNull
    @Column(name = "total_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NotNull
    @Enumerated(EnumType.STRING)
    @ColumnDefault("'ACCEPTED'")
    @Column(name = "status", nullable = false, length = 20)
    private OrderStatus status;

    @Column(name = "comment", length = Integer.MAX_VALUE)
    private String comment;

    @OneToMany(mappedBy = "order")
    private Set<OrderProduct> orderProducts = new LinkedHashSet<>();
}

