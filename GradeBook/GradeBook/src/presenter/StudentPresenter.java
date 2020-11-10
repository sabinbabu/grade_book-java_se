/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package presenter;

import java.util.List;
import model.ConnectionException;
import static model.GradeBookModel.Query.*;
import model.IConnect;
import model.IQuery;
import model.QueryException;
import model.Student;
import view.IView;

/**
 *
 * @author PC
 */
public class StudentPresenter {

    // The context for model and view interaction
    IView view;
    IQuery queries;
    IConnect connector;
    ViewModel viewmodel;

    public StudentPresenter(IView iv, IQuery iq, IConnect ic) {
        // initialise controller access
        view = iv;
        // intialise model access
        queries = iq;
        connector = ic;
        // initialise the browsing context
        viewmodel = new ViewModel();
    }

    /**
     * Insert a new entry into the grade book.
     *
     *
     */
    public void insert(String studentId, String assignment1, String assignment2, String exam, String total, String grade) throws IllegalArgumentException {

        if (studentId.equals("") || assignment1.equals("") || assignment2.equals("") || exam.equals("") || total.equals("") || grade.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {

            Student s = new Student(Integer.parseInt(studentId), Double.parseDouble(assignment1), Double.parseDouble(assignment2), Double.parseDouble(exam), Double.parseDouble(total), grade);
            int result = queries.command(INSERT, s);
            if (result == 1) {
                view.displayMessage("Student added");
                view.displayTextArea("1 student added !!\n STUDENTID = " + studentId + "\n ASSIGNMENT1 = " + assignment1 + "\n ASSIGNMENT2 = " + assignment2 + "\n EXAM = " + exam + "\n TOTAL = " + total + "\n GRADE = " + grade);
            } else {
                view.displayMessage("Student not added");
            }
            view.setBrowsing(false);

        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Update an existing record of grade book.
     *
     *
     */
    public void update(String studentId, String assignment1, String assignment2, String exam, String total, String grade) throws IllegalArgumentException {

        if (studentId.equals("") || assignment1.equals("") || assignment2.equals("") || exam.equals("") || total.equals("") || grade.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {

            Student s = new Student(Integer.parseInt(studentId), Double.parseDouble(assignment1), Double.parseDouble(assignment2), Double.parseDouble(exam), Double.parseDouble(total), grade);
            int result = queries.command(UPDATE, s);
            if (result == 1) {
                view.displayMessage("Student updated");
                view.displayTextArea("1 student updated !!\n STUDENTID = " + studentId + "\n ASSIGNMENT1 = " + assignment1 + "\n ASSIGNMENT2 = " + assignment2 + "\n EXAM = " + exam + "\n TOTAL = " + total + "\n GRADE = " + grade);
            } else {
                view.displayMessage("Student not updated");
            }
            view.setBrowsing(false);

        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Calculate grade for the students
     *
     *
     */
    public void calculate(String total) {
        if (total.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        Double totalMarks = Double.parseDouble(total);
        if (totalMarks >= 85.0 && totalMarks <= 100.0) {
            view.displayGrade("HD");
        } else if (totalMarks >= 75.0 && totalMarks < 85.0) {
            view.displayGrade("D");
        } else if (totalMarks >= 65.0 && totalMarks < 75.0) {
            view.displayGrade("C");
        } else if (totalMarks >= 50.0 && totalMarks < 65.0) {
            view.displayGrade("P");
        } else if (totalMarks >= 45.0 && totalMarks <= 50.0) {
            view.displayGrade("SE");
        } else {
            view.displayGrade("F");
        }
    }

    /**
     * Retrieves the students with given tolerance value
     *
     *
     */
    public void toleranceValue(String value) {
        if (value.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {
            List results = queries.select(TOLERANCEVALUE, value);
            if (results.isEmpty()) {
                view.displayTextArea("No records found");
            } else {
                viewmodel.set(results);
                displayCurrentRecord(results);
            }

        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Retrieves the students with given tolerance range
     *
     *
     */
    public void toleranceRange(String minValue, String maxValue) {
        if (minValue.equals("") && maxValue.equals("")) {
            throw new IllegalArgumentException("Arguments must not contain an empty string");
        }
        try {
            List results = queries.select(TOLERANCERANGE, minValue, maxValue);
            if (results.isEmpty()) {
                view.displayTextArea("No records found");
            } else {
                viewmodel.set(results);
                displayCurrentRecord(results);
            }

        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }

    }

    /**
     * Retrieves the student with given student ID
     *
     *
     */
    public void findById(String id) {

        if (id.equals("")) {
            throw new IllegalArgumentException("Argument must not be an empty string");
        }
        try {
            List results = queries.select(STUDENTID, id);
            if (results.isEmpty()) {
                view.displayTextArea("No records found");
            } else {
                viewmodel.set(results);
                displayCurrentRecord(results);
            }
        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Retrieves the students with given grade
     *
     *
     */
    public void findByGrade(String grade) {

        if (grade.equals("")) {
            throw new IllegalArgumentException("Argument must not be an empty string");
        }
        if ("HD".equals(grade) || "D".equals(grade) || "C".equals(grade) || "P".equals(grade) || "SE".equals(grade) || "F".equals(grade)) {

            try {
                List results = queries.select(GRADE, grade);
                if (results.isEmpty()) {
                    view.displayTextArea("No records found");
                } else {
                    viewmodel.set(results);
                    displayCurrentRecord(results);
                }

            } catch (QueryException e) {
                view.displayTextArea("No records found");
                view.displayError(e.getMessage());
                System.exit(1);
            }
        } else {
            view.displayTextArea("Please enter a valid grade");
            System.out.println("Please enter a valid grade");
        }

    }

    /**
     * Retrieves all the students
     *
     *
     */
    public void selectAll(Boolean initTrue) {
        try {
            List results = queries.select(ALL);
            viewmodel.set(results);
            if (initTrue) {
                view.notifySize(viewmodel.current());
            } else {
                displayCurrentRecord(results);
            }

        } catch (QueryException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }

    /**
     * Displays data to UI
     *
     *
     */
    private void displayCurrentRecord(List results) {
        if (results.isEmpty()) {
            view.displayMessage("No records found");
            view.setBrowsing(false);
            return;
        }
        view.displayRecord(viewmodel.current());
        view.setBrowsing(true);
    }

    /**
     * show Next record in UI
     *
     *
     */
    public void showNext() {
        view.displayRecord(viewmodel.next());
    }

    /**
     * show Previous record in UI
     *
     *
     */
    public void showPrevious() {
        view.displayRecord(viewmodel.previous());
    }

    /**
     * Close the address book.
     */
    public void close() {
        try {
            connector.disconnect();
        } catch (ConnectionException e) {
            view.displayError(e.getMessage());
            System.exit(1);
        }
    }
}
