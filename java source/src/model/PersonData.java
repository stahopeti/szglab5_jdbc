package model;

import java.time.LocalDate;

import dal.exceptions.ValidationException;

public class PersonData {
    public PersonData() {
    
    }
    private Integer person_id;

    public Integer getPerson_id() {
        return person_id;
    }

    public void setPerson_id(Integer person_id) {
        this.person_id = person_id;
    }

    public void parsePerson_id(String person_id) throws ValidationException {
        //TODO: validate
        this.person_id = Integer.parseInt(person_id);
    }
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void parseName(String name) throws ValidationException {
        //TODO: validate
        this.name = name;
    }
    private String address;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void parseAddress(String address) throws ValidationException {
        //TODO: validate
        this.address = address;
    }
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void parsePhone(String phone) throws ValidationException {
        //TODO: validate
        this.phone = phone;
    }
    private Integer income;

    public Integer getIncome() {
        return income;
    }

    public void setIncome(Integer income) {
        this.income = income;
    }

    public void parseIncome(String income) throws ValidationException {
        //TODO: validate
        this.income = Integer.parseInt(income);
    }
    private String hobby;

    public String getHobby() {
        return hobby;
    }

    public void setHobby(String hobby) {
        this.hobby = hobby;
    }

    public void parseHobby(String hobby) throws ValidationException {
        //TODO: validate
        this.hobby = hobby;
    }
    private String favourite_movie;

    public String getFavourite_movie() {
        return favourite_movie;
    }

    public void setFavourite_movie(String favourite_movie) {
        this.favourite_movie = favourite_movie;
    }

    public void parseFavourite_movie(String favourite_movie) throws ValidationException {
        //TODO: validate
        this.favourite_movie = favourite_movie;
    }

}