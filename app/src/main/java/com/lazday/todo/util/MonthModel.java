package com.lazday.todo.util;

public class MonthModel {
    private Integer number;
    private String name;

    public MonthModel(Integer number, String name) {
        this.number = number;
        this.name = name;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MonthModel{" +
                "number=" + number +
                ", name='" + name + '\'' +
                '}';
    }
}
