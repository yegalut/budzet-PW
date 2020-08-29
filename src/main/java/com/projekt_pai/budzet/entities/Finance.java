package com.projekt_pai.budzet.entities;

import javax.persistence.*;

@Entity
@Table(name = "finance")
public class Finance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column
    private String type;
    @Column
    private Integer amount;
    @Column
    private String date;
    @Column
    private Integer categoryId;
    @Column(name = "user_id")
    private Integer userId;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Integer getcategoryId() {
        return categoryId;
    }

    public void setcategoryId(Integer categoryId) {
        this.categoryId = categoryId;
    }

    public Integer getuserId() {
        return userId;
    }

    public void setuserId(Integer userId) {
        this.userId = userId;
    }

    public Finance() {
    }

    public Finance(String name, String type, Integer amount, String date, Integer categoryId, Integer userId) {
        this.name = name;
        this.type = type;
        this.amount = amount;
        this.date = date;
        this.categoryId = categoryId;
        this.userId = userId;
    }
}
