package com.microshop.domain;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by min on 21/11/2016.
 */

public class ProductDTO implements Serializable {

    private Long id;
    private BigDecimal price;

    public ProductDTO(Long id, BigDecimal price) {
        this.id = id;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
