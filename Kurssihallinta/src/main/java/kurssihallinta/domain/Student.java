/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

/**
 *
 * @author okkokuisma
 */
public class Student {
    private String firstName;
    private String surname;
    private String id;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    private String email;
    
    public Student() {
    }
    
    /**
    * Creates a Student object with the given contact information.
    * 
     * @param firstName First name
     * @param surname Surname
     * @param id Personal ID number
     * @param address Address
     * @param zipCode ZIP code
     * @param city City of residence
     * @param country Country of residence
     * @param email Email address
    */
    public Student(String firstName, String surname, String id, String address, String zipCode, String city, String country, String email) {
        this.firstName = firstName;
        this.surname = surname;
        this.id = id;
        this.address = address;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surname;
    }

    public String getId() {
        return id;
    }

    public String getAddress() {
        return address;
    }

    public String getZipCode() {
        return zipCode;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
}
