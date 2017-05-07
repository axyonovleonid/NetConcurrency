package database;

import java.io.Serializable;

/**
 * NetConcurrency created by лёня on 07.05.2017.
 */
public class Teacher implements Serializable{
    public String name, post, faculty, univertsity;
    public double x_coord, y_coord;
    Teacher(String name, String post, String faculty, String univ, double x, double y){
        this.name = name;
        this.post = post;
        this.faculty = faculty;
        this.univertsity = univ;
        x_coord = x;
        y_coord = y;
    }

    public String toString (){
        return name + ": " + post + ", " + faculty + ", " + univertsity + '\0';
    }
}
