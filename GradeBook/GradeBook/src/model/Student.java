/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

/**
 * Student data class
 *
 */
public class Student {

    private int studentId;
    private double assignment1;
    private double assignment2;
    private double exam;
    private double total;
    private String grade;

    /**
     * Create a data transfer object for an grade book record.
     *
     *
     */
    public Student(int studentId, double assignment1, double assignment2, double exam, double total, String grade) {
        this.studentId = studentId;
        this.assignment1 = assignment1;
        this.assignment2 = assignment2;
        this.exam = exam;
        this.total = total;
        this.grade = grade;
    }

    public void setStudentID(int studentId) {
        this.studentId = studentId;
    }

    public void setAssignment1(Double assignment1) {
        this.assignment1 = assignment1;
    }

    public void setAssignment2(Double assignment2) {
        this.assignment2 = assignment2;
    }

    public void setExam(Double exam) {
        this.exam = exam;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
    // Getters. 

    /**
     * @return record identifier
     */
    public int getStudentID() {
        return studentId;
    }

    /**
     * @return assignment1
     */
    public double getAssignment1() {
        return assignment1;
    }

    /**
     * @return assignment2
     */
    public double getAssignment2() {
        return assignment2;
    }

    /**
     * @return exam
     */
    public double getExam() {
        return exam;
    }

    /**
     * @return total
     */
    public double getTotal() {
        return total;
    }

    /**
     * @return grade
     */
    public String getGrade() {
        return grade;
    }
}
