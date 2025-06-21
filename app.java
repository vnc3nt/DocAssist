import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class app extends Application {
    public void start(Stage mystage){
        Page base = new WelcomePage(mystage);
        
        base.setStyle("-fx-padding: 30; -fx-font-family: 'Arial'; -fx-font-size: 16");

        Scene scene = new Scene(base, 720, 480);
        mystage.setScene(scene) ;
        mystage.setTitle("DocAssist");
        mystage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}