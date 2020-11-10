/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.Student;
import presenter.IndexedStudent;
import presenter.StudentPresenter;

/**
 *
 * @author PC
 */
public class GradeBookFXMLDocumentController implements Initializable, IView<IndexedStudent> {

    StudentPresenter studentPresenter;

    private Label label;
    @FXML
    private Button previousButton;
    @FXML
    private Button nextButton;
    @FXML
    private Button findButton;
    @FXML
    private Button insertButton;
    @FXML
    private TextField currentIndex;
    @FXML
    private TextField finalIndex;
    @FXML
    private TextField idTextField;
    @FXML
    private Button calculateButton;
    @FXML
    private TextField assignment1TextField;
    @FXML
    private TextField examTextField;
    @FXML
    private TextField assignment2TextField;
    @FXML
    private TextField totalTextField;
    @FXML
    private TextField findIDTextField;
    @FXML
    private TextField gradeTextField;
    @FXML
    private TextArea displayArea;
    @FXML
    private Button browse;
    @FXML
    private Button updateButton;
    @FXML
    private TextField findIDTextField1;
    @FXML
    private Button findButtonTolerance;
    @FXML
    private TextField findGradeTextField;
    @FXML
    private Button searchButton;
    @FXML
    private Button clearButton;

    private final Boolean initTrue = true;
    @FXML
    private Button start;
    @FXML
    private TextField toleranceMinValue;
    @FXML
    private Button findButtonToleranceRange;
    @FXML
    private TextField toleranceMaxValue;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        setBrowsing(false);

    }

    public void bind(StudentPresenter pp) {
        studentPresenter = pp;
    }

    @FXML
    private void previous(ActionEvent event) {
        studentPresenter.showPrevious();
    }

    @FXML
    private void next(ActionEvent event) {
        studentPresenter.showNext();
    }

    /**
     * Find Student By ID
     *
     *
     */
    @FXML
    private void find(ActionEvent event) {
        updateButton.setDisable(false);
        insertButton.setDisable(true);
        studentPresenter.findById(findIDTextField.getText());
    }

    /**
     * Insert record to database
     *
     *
     */
    @FXML
    private void insert(ActionEvent event) {
        studentPresenter.insert(
                idTextField.getText(),
                assignment1TextField.getText(),
                assignment2TextField.getText(),
                examTextField.getText(),
                totalTextField.getText(),
                gradeTextField.getText()
        );
    }

    /**
     * Calculates the grade of the student
     *
     *
     */
    @FXML
    private void calculate(ActionEvent event) {
        studentPresenter.calculate(totalTextField.getText());
    }

    @Override
    public void displayRecord(IndexedStudent indexedStudent) {
        Student student = indexedStudent.getStudent();
        idTextField.setText(Integer.toString(student.getStudentID()));
        assignment1TextField.setText(Double.toString(student.getAssignment1()));
        assignment2TextField.setText(Double.toString(student.getAssignment2()));
        examTextField.setText(Double.toString(student.getExam()));
        totalTextField.setText(Double.toString(student.getTotal()));
        gradeTextField.setText(student.getGrade());
        finalIndex.setText(Integer.toString(indexedStudent.getSize()));
        currentIndex.setText(Integer.toString(indexedStudent.getIndex()));
        displayArea.setText(" STUDENTID = " + Integer.toString(student.getStudentID()) + "\n ASSIGNMENT1 = " + Double.toString(student.getAssignment1()) + "\n ASSIGNMENT2 = " + Double.toString(student.getAssignment2()) + "\n EXAM = " + Double.toString(student.getExam()) + "\n TOTAL = " + Double.toString(student.getTotal()) + "\n GRADE = " + student.getGrade());
    }

    /**
     * Displays grade to UI
     *
     *
     */
    @Override
    public void displayGrade(String s) {
        gradeTextField.setText(s);
    }

    /**
     * Displays information in Alert box
     *
     *
     */
    @Override
    public void displayMessage(String s) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(s);
        alert.show();
    }

    @Override
    public void setBrowsing(boolean flag) {
        nextButton.setDisable(!flag);
        previousButton.setDisable(!flag);
    }

    @Override
    public void displayError(String e) {
        System.err.println(e);
    }

    /**
     * View records of all the students
     *
     *
     */
    @FXML
    private void browse(ActionEvent event) {
        studentPresenter.selectAll(false);
        updateButton.setDisable(true);
    }

    /**
     * Update record of particular student
     *
     *
     */
    @FXML
    private void update(ActionEvent event) {
        updateButton.setDisable(true);
        insertButton.setDisable(false);
        studentPresenter.update(
                idTextField.getText(),
                assignment1TextField.getText(),
                assignment2TextField.getText(),
                examTextField.getText(),
                totalTextField.getText(),
                gradeTextField.getText()
        );
    }

    /**
     * Displays students with certain tolerance value
     *
     *
     */
    @FXML
    private void findToleranceValue(ActionEvent event) {
        studentPresenter.toleranceValue(findIDTextField1.getText());
    }

    /**
     * Displays text in result area
     *
     *
     */
    @Override
    public void displayTextArea(String t) {
        displayArea.setText(t);
    }

    /**
     * Displays students with certain grade
     *
     *
     */
    @FXML
    private void searchByGrade(ActionEvent event) {
        studentPresenter.findByGrade(findGradeTextField.getText().toUpperCase());
    }

    /**
     * Resets the view
     *
     *
     */
    @FXML
    private void clearButton(ActionEvent event) {
        setBrowsing(false);
        displayArea.clear();
        findGradeTextField.clear();
        findIDTextField1.clear();
        gradeTextField.clear();
        findIDTextField.clear();
        totalTextField.clear();
        assignment2TextField.clear();
        examTextField.clear();
        assignment1TextField.clear();
        idTextField.clear();
        finalIndex.clear();
        currentIndex.clear();
        toleranceMinValue.clear();
        toleranceMaxValue.clear();
        updateButton.setDisable(true);
        insertButton.setDisable(false);
    }

    /**
     * Displays the total number of student record
     *
     *
     */
    @Override
    public void notifySize(IndexedStudent indexedStudent) {
        displayArea.setText("The number of records in the database = " + Integer.toString(indexedStudent.getSize()));
    }

    @FXML
    private void startButton(ActionEvent event) {
        start.setDisable(true);
        findButton.setDisable(false);
        insertButton.setDisable(false);
        calculateButton.setDisable(false);
        browse.setDisable(false);
        findButtonToleranceRange.setDisable(false);
        findButtonTolerance.setDisable(false);
        searchButton.setDisable(false);
        clearButton.setDisable(false);
        studentPresenter.selectAll(initTrue);
    }

    /**
     * Displays students with certain tolerance range
     *
     *
     */
    @FXML
    private void findToleranceRange(ActionEvent event) {
        studentPresenter.toleranceRange(toleranceMinValue.getText(),
                toleranceMaxValue.getText());
    }
}
