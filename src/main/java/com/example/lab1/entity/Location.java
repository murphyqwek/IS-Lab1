package com.example.lab1.entity;

import jakarta.validation.constraints.NotNull;

public class Location {
    private float x;
    private long y;
    @NotNull
    private Long z; //Поле не может быть null
    private String name; //Длина строки не должна быть больше 692, Поле может быть null
}
