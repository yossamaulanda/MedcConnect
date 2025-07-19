package App;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class controller00 implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button loginButton;

    @FXML
    private Button backButton;

    @FXML
    private Hyperlink registerHyperlink;

    private UserList userList = new UserList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.loadUser(); 

        txtUsername.getStyleClass().add("text-field");
        txtPassword.getStyleClass().add("password-field");
        loginButton.getStyleClass().add("loginBtn");
        backButton.getStyleClass().add("backBtn");
        registerHyperlink.getStyleClass().add("hyperlink");
        
        addHoverEffects();
        
        addFocusEffects();
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        animateButtonPress(backButton);
        directToHalaman0();
    }

    @FXML
    private void handleLoginButton(ActionEvent event) {
        animateButtonPress(loginButton);
        loginButton.setDisable(true);

        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();
        if (username.isEmpty() || password.isEmpty()) {
            highlightEmptyFields();
            showStyledAlert(AlertType.ERROR, "Login Failed", "Username and password cannot be empty.");
            loginButton.setDisable(false);
            return;
        }

        removeErrorStyling();

        UserData loggedInUser = isValidLogin(username, password);

        if (loggedInUser != null) {
            addSuccessStyling();
            showStyledAlert(AlertType.INFORMATION, "Login Success", "Welcome " + loggedInUser.getNama() + "!");

            if ("Dokter".equals(loggedInUser.getRole())) {
                directToStoreDashboard();
            } else if ("Pasien".equals(loggedInUser.getRole())) {
                directToPatientDashboard();
            } else {
                directToStoreDashboard();
            }
        } else {
            addErrorStyling();
            showStyledAlert(AlertType.ERROR, "Login Failed", "Username or password not registered.");
            loginButton.setDisable(false);
        }
    }

    @FXML
    private void handleRegisterHyperlink(ActionEvent event) {
        animateHyperlink(registerHyperlink);
        directToRegistrationPage();
    }

    private void directToHalaman0() {
        try {
            OpenScene openScene = new OpenScene();
            Pane halaman = openScene.getPane("halaman0.fxml");
            
            // Add transition animation
            addPageTransition(halaman);
            
            mainPane.setCenter(halaman);
            System.out.println("Redirected to halaman0");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load halaman0: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void directToRegistrationPage() {
        try {
            OpenScene openScene = new OpenScene();
            Pane halaman = openScene.getPane("halaman000.fxml");
            
            // Add transition animation
            addPageTransition(halaman);
            
            mainPane.setCenter(halaman);
            System.out.println("Redirected to registration page");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load registration page: " + e.getMessage());
        }
    }

    private void directToStoreDashboard() {
        try {
            OpenScene openScene = new OpenScene();
            Pane halaman = openScene.getPane("halaman1.fxml");
            
            // Add transition animation
            addPageTransition(halaman);
            
            mainPane.setCenter(halaman);
            System.out.println("Redirected to doctor dashboard");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load doctor dashboard: " + e.getMessage());
        }
    }

    private void directToPatientDashboard() {
        try {
            OpenScene openScene = new OpenScene();
            Pane halaman = openScene.getPane("halaman1.fxml");
            
            // Add transition animation
            addPageTransition(halaman);
            
            mainPane.setCenter(halaman);
            System.out.println("Redirected to patient dashboard");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load patient dashboard: " + e.getMessage());
        }
    }

    private void showStyledAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        
        // Add custom styling to alert
        alert.getDialogPane().setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #f48c2a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );
        
        alert.showAndWait();
    }

    private UserData isValidLogin(String username, String password) {
        // Check if userList is not null and has content
        if (userList != null && !userList.getUserList().isEmpty()) {
            for (UserData user : userList.getUserList()) {
                // Check main user credentials
                if (username.equals(user.getUsername()) && password.equals(user.getPassword())) {
                    UserList.setLoginAccount(user);
                    return user;
                }

                // Check family members if they exist
                if (user.getFamily() != null) {
                    for (UserData family : user.getFamily()) {
                        if (username.equals(family.getUsername()) && password.equals(family.getPassword())) {
                            UserList.setLoginAccount(family);
                            return family;
                        }
                    }
                }
            }
        }
        return null;
    }

    // Animation and styling methods
    private void addHoverEffects() {
        // Button hover effect
        loginButton.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), loginButton);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
        });

        loginButton.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), loginButton);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        // Back button hover effect
        backButton.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), backButton);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
        });

        backButton.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), backButton);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        // Hyperlink hover effect
        registerHyperlink.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), registerHyperlink);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });

        registerHyperlink.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), registerHyperlink);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    private void addFocusEffects() {
        // Username field focus effect
        txtUsername.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                txtUsername.setStyle(txtUsername.getStyle() + "-fx-border-color: #f48c2a; -fx-border-width: 2;");
            } else {
                txtUsername.setStyle(txtUsername.getStyle().replace("-fx-border-color: #f48c2a; -fx-border-width: 2;", ""));
            }
        });

        // Password field focus effect
        txtPassword.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                txtPassword.setStyle(txtPassword.getStyle() + "-fx-border-color: #f48c2a; -fx-border-width: 2;");
            } else {
                txtPassword.setStyle(txtPassword.getStyle().replace("-fx-border-color: #f48c2a; -fx-border-width: 2;", ""));
            }
        });
    }

    private void animateButtonPress(Button button) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(50), button);
        scaleTransition.setToX(0.95);
        scaleTransition.setToY(0.95);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void animateHyperlink(Hyperlink hyperlink) {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), hyperlink);
        scaleTransition.setToX(1.1);
        scaleTransition.setToY(1.1);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void addPageTransition(Pane newPage) {
        // Fade in transition
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), newPage);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        
        // Slide in transition
        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), newPage);
        slideTransition.setFromX(50);
        slideTransition.setToX(0);
        
        fadeTransition.play();
        slideTransition.play();
    }

    private void highlightEmptyFields() {
        if (txtUsername.getText().trim().isEmpty()) {
            txtUsername.getStyleClass().add("error-field");
        }
        if (txtPassword.getText().isEmpty()) {
            txtPassword.getStyleClass().add("error-field");
        }
    }

    private void removeErrorStyling() {
        txtUsername.getStyleClass().remove("error-field");
        txtPassword.getStyleClass().remove("error-field");
    }

    private void addErrorStyling() {
        txtUsername.getStyleClass().add("error-field");
        txtPassword.getStyleClass().add("error-field");
    }

    private void addSuccessStyling() {
        txtUsername.getStyleClass().add("success-field");
        txtPassword.getStyleClass().add("success-field");
    }
}