/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author okkokuisma
 */
public class Course {
    private String name;
//    private StringProperty nameSP;
    private LocalDate startDate;
    private LocalDate endDate;
    private String teacher;

    public Course(String name, LocalDate startDate, LocalDate endDate, String teacher) {
        this.name = name;
        this.startDate = startDate;
        this.endDate = endDate;
        this.teacher = teacher;
//        nameSP = new SimpleStringProperty(this, "name");
//        nameSP.set(name);
    }
    
//    public StringProperty nameProperty() {
//        return nameSP;
//    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartDate() {
        return startDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate.format(DateTimeFormatter.ISO_DATE);
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }
    
    
}
