/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kurssihallinta.domain;

public class Classroom {
    private String name;
    
    public Classroom(){
    }
    
    /**
    * Creates a Classroom object with the given name value.
    * 
     * @param name Classroom name
    */
    public Classroom(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }  
}
