package App;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.Pane;
import java.io.IOException;

public class OpenScene {
    
    public Pane getPane(String fxmlFile) {
        try {
            System.out.println("Attempting to load FXML file: " + fxmlFile);
            if (getClass().getResource(fxmlFile) == null) {
                System.out.println("ERROR: FXML file not found: " + fxmlFile);
                return null;
            }
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Pane pane = loader.load();
            
            System.out.println("Successfully loaded FXML file: " + fxmlFile);
            return pane;
            
        } catch (IOException e) {
            System.out.println("ERROR loading FXML file: " + fxmlFile);
            e.printStackTrace();
            return null;
        }
    }
}