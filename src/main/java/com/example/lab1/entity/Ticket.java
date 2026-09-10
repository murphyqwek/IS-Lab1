package com.example.lab1.entity;

public class Ticket {
    private int id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.ZonedDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Person person; //Поле может быть null
    private Event event; //Поле не может быть null
    private int price; //Значение поля должно быть больше 0
    private TicketType type; //Поле может быть null
    private int discount; //Значение поля должно быть больше 0, Максимальное значение поля: 100
    private Float number; //Поле может быть null, Значение поля должно быть больше 0
    private Venue venue; //Поле может быть null
}
