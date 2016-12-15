package com.microshop.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.microshop.utils.JsonDateSerializer;
import org.springframework.context.annotation.Lazy;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by min on 18/10/2016.
 */

@Entity
@Table(name="products")
public class Product implements Serializable {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long id;

    @Column(nullable = false, unique = true)
    private String sku;

    @Column(nullable = false)
    private String name;

    private String description;

    @Column(nullable = false)
    private BigDecimal price = new BigDecimal("0.0");

    private String imageUrl;

    private boolean disabled;

    @Column(name="created_on")
    private Date createdOn;

    @Column(name="cat_id")
    private String cat_id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="seller_id")
    private Seller seller;

//    @JsonIgnore
//    @ManyToOne
//    @JoinColumn(name="cat_id")
//    private Category category;

    @JsonIgnore
    @Transient
    private final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");


//    public Product(String title, Date created, String summary, Integer prize) throws ParseException{
//        this.title = title;
//        this.created = created;
//        this.summary = summary;
//        this.prize = prize;
//    }

    @JsonSerialize(using=JsonDateSerializer.class)
    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isDisabled() {
        return disabled;
    }

    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
    }

    public Seller getSeller() {
        return seller;
    }

    public void setSeller(Seller seller) {
        this.seller = seller;
    }

    //    @JsonIgnore
//    public String getCreatedAsShort(){
//        return format.format(created_on);
//    }
}
