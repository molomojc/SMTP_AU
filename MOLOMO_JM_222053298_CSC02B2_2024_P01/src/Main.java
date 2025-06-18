

import javafx.stage.Stage;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * @author Jm Molomo
 * 
 */


public class Main extends Application {
	@Override
	public void start(Stage primaryStage) {
		try {
			
		
			 MyPane rooti = new MyPane();
			Scene scene = new Scene(rooti);
		
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
	}
}
