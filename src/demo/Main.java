package demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.opencv.core.Core;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Controller.fxml"));
        BorderPane root = (BorderPane) loader.load();
        root.setStyle("-fx-background-color: whitesmoke;");
        Scene scene = new Scene(root, 1440, 810);
        //Scene scene = new Scene(root, 1024, 768);
        primaryStage.setTitle("JVisionDetection Demo");
        primaryStage.setScene(scene);
        // show the GUI
        primaryStage.show();

    }

    public static void main(String[] args) {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);


        System.out.println(Core.getBuildInformation());


        launch(args);
    }
}
