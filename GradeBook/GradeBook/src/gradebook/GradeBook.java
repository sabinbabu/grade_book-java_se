/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gradebook;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.ConnectionException;
import model.GradeBookModel;
import presenter.StudentPresenter;
import view.GradeBookFXMLDocumentController;
import view.IView;

/**
 *
 * @author PC
 */
public class GradeBook extends Application {

    private final String SYSTEM_TITLE = "GradeBook";

    @Override
    public void start(Stage stage) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GradeBookFXMLDocument.fxml"));
        Parent root = loader.load();
        GradeBookFXMLDocumentController controller = loader.getController();

        GradeBookModel gradeBookModel = new GradeBookModel();
        try {
             // connecting to database   
            gradeBookModel.connect();
            gradeBookModel.initialise();
        } catch (ConnectionException e) {
            System.err.println(e.getMessage());
            e.getCause().printStackTrace();
            System.exit(1);
        }
        
        StudentPresenter studentPresenter = new StudentPresenter(controller, gradeBookModel, gradeBookModel);
        controller.bind(studentPresenter);

        Scene scene = new Scene(root);
        stage.setTitle(SYSTEM_TITLE);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
