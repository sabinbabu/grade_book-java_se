/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

import model.Student;

/**
 *
 * @author PC
 */
public class IndexedStudent {

    private Student s;
    private int i;
    private int n;
    private String grade;

    /**
     * Create a wrapper for a Student object
     *
     * @param s the object to be wrapped
     * @param i the position of the object in the browsing context
     * @param n the number of objects in the browsing context
     */
    public IndexedStudent(Student s, int i, int n) {
        this.s = s;
        this.i = i;
        this.n = n;

    }

    /**
     * @return the student object being wrapped
     */
    public Student getStudent() {
        return s;
    }

    /**
     * @return the position of the wrapped student object in the browsing
     * context
     */
    public int getIndex() {
        return i;
    }

    /**
     * @return the number of objects in the browsing context
     */
    public int getSize() {
        return n;
    }

}
