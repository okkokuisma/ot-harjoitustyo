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
    private int dbId;
    private String firstName;
    private String surname;
    private String id;
    private String address;
    private String zipCode;
    private String city;
    private String country;
    private String email;

    public Student(int dbId, String firstName, String surname, String id, String address, String zipCode, String city, String country, String email) {
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

    public int getDbId() {
        return dbId;
    }
    
    
}