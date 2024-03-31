package com.patika.kredinbizdeservice.model;

import lombok.Data;

@Data
public class Bank {
    private Long id;
    private String name;

    public Bank(Long id, String name) {
        this.id = id;
        this.name = name;
    }
}
