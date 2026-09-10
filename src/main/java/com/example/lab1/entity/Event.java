package com.example.lab1.entity;

public class Event {
    private Integer id; //Поле не может быть null, Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private String description; //Поле не может быть null
    private EventType eventType; //Поле может быть null
}