package App;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.StaxDriver;

public class controller00000 implements Initializable {

    @FXML
    private BorderPane mainPane;

    @FXML
    private ComboBox<String> cbRole;

    @FXML
    private Label labelDokter;

    @FXML
    private TextField txtDokterName;

    @FXML
    private TextField txtName;

    @FXML
    private TextField txtUsername;

    @FXML
    private TextField txtEmail;

    @FXML
    private TextField tfUsia;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private PasswordField txtConfirmPassword;

    @FXML
    private Button registerButton;

    @FXML
    private Button backButton;

    @FXML
    private Hyperlink loginHyperlink;

    private UserList userList = new UserList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        userList.loadUser();
        cbRole.getItems().addAll("Dokter", "Pasien");
        cbRole.setValue("Dokter");
        txtDokterName.setVisible(false);
        labelDokter.setVisible(false);
        setupStyling();
        addHoverEffects();
        addFocusEffects();
    }

    @FXML
    private void handleBackButton(ActionEvent event) {
        animateButtonPress(backButton);
        directToController00000();
    }

    @FXML
    private void handleRoleChange(ActionEvent event) {
        String role = cbRole.getValue();
        txtDokterName.setVisible(false);
        labelDokter.setVisible(false);
        txtDokterName.clear();
        animateRoleChange();
    }

    @FXML
    private void handleRegisterButton(ActionEvent event) {
        animateButtonPress(registerButton);
        registerButton.setDisable(true);
        String name = txtName.getText().trim();
        String username = txtUsername.getText().trim();
        String email = txtEmail.getText().trim();
        String password = txtPassword.getText();
        String confirmPassword = txtConfirmPassword.getText();
        String usia = tfUsia.getText().trim();
        String role = cbRole.getValue();

        removeErrorStyling();

        if (name.isEmpty() || username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || usia.isEmpty()) {
            highlightEmptyFields();
            showStyledAlert(AlertType.ERROR, "Registration Failed", "All fields must be filled out.");
            registerButton.setDisable(false);
            return;
        }

        try {
            int age = Integer.parseInt(usia);
            if (age < 0 || age > 150) {
                highlightField(tfUsia);
                showStyledAlert(AlertType.ERROR, "Registration Failed", "Please enter a valid age (0-150).");
                registerButton.setDisable(false);
                return;
            }
        } catch (NumberFormatException e) {
            highlightField(tfUsia);
            showStyledAlert(AlertType.ERROR, "Registration Failed", "Age must be a valid number.");
            registerButton.setDisable(false);
            return;
        }

        if ("Dokter".equalsIgnoreCase(role)) {
            if (email.isEmpty()) {
                highlightField(txtEmail);
                showStyledAlert(AlertType.ERROR, "Registration Failed", "Email is required for Doctor accounts.");
                registerButton.setDisable(false);
                return;
            }
            
            if (!isValidEmail(email)) {
                highlightField(txtEmail);
                showStyledAlert(AlertType.ERROR, "Registration Failed", "Invalid email format.");
                registerButton.setDisable(false);
                return;
            }

            if (isEmailTaken(email)) {
                highlightField(txtEmail);
                showStyledAlert(AlertType.ERROR, "Registration Failed", "Email already registered.");
                registerButton.setDisable(false);
                return;
            }
        }

        if (password.length() < 8) {
            highlightField(txtPassword);
            showStyledAlert(AlertType.ERROR, "Registration Failed", "Password must be at least 8 characters long.");
            registerButton.setDisable(false);
            return;
        }

        if (!password.equals(confirmPassword)) {
            highlightField(txtPassword);
            highlightField(txtConfirmPassword);
            showStyledAlert(AlertType.ERROR, "Registration Failed", "Passwords do not match.");
            registerButton.setDisable(false);
            return;
        }

        if (isUsernameTaken(username)) {
            highlightField(txtUsername);
            showStyledAlert(AlertType.ERROR, "Registration Failed", "Username already taken.");
            registerButton.setDisable(false);
            return;
        }

        addSuccessStyling();
        if ("Dokter".equalsIgnoreCase(role)) {
            registerDokterUser(name, username, email, password, usia);
        } else if ("Pasien".equalsIgnoreCase(role)) {
            registerPasienUser(name, username, password, usia);
        }
        registerButton.setDisable(false);
    }

    @FXML
    private void handleLoginHyperlink(ActionEvent event) {
        animateHyperlink(loginHyperlink);
        directToController00000Login();
    }

    private boolean isValidEmail(String email) {
        String emailRegex = "^[a-zA-Z0-9_]+(?:\\.[a-zA-Z0-9_]+)*@(?:[a-zA-Z0-9]+\\.)+[a-zA-Z]{2,7}$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    private boolean isUsernameTaken(String username) {
        if (userList != null && !userList.getUserList().isEmpty()) {
            for (UserData user : userList.getUserList()) {
                if (username.equals(user.getUsername())) {
                    return true;
                }
                
                if (user.getFamily() != null) {
                    for (UserData family : user.getFamily()) {
                        if (username.equals(family.getUsername())) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    private boolean isEmailTaken(String email) {
        if (userList != null && !userList.getUserList().isEmpty()) {
            for (UserData user : userList.getUserList()) {
                if (email.equals(user.getEmail())) {
                    return true;
                }
            }
        }
        return false;
    }

    private void showStyledAlert(AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.getDialogPane().setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: #f48c2a;" +
            "-fx-border-width: 2;" +
            "-fx-border-radius: 10;" +
            "-fx-background-radius: 10;"
        );
        
        alert.showAndWait();
    }

    private void directToController00000() {
        try {
            OpenScene openScene = new OpenScene();
            Pane page = openScene.getPane("halaman0000.fxml");
            addPageTransition(page);
            mainPane.setCenter(page);
            System.out.println("Redirected to halaman0000 page");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load halaman0000 page: " + e.getMessage());
        }
    }

    private void directToController00000Login() {
        try {
            OpenScene openScene = new OpenScene();
            Pane page = openScene.getPane("controller00000.fxml");
            addPageTransition(page);
            mainPane.setCenter(page);
            System.out.println("Redirected to controller00000 login page");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load controller00000 login page: " + e.getMessage());
        }
    }
    private void directToHalaman1() {
        try {
            OpenScene openScene = new OpenScene();
            Pane page = openScene.getPane("halaman1.fxml");
            addPageTransition(page);
            mainPane.setCenter(page);
            System.out.println("Redirected to halaman1 page");
        } catch (Exception e) {
            showStyledAlert(AlertType.ERROR, "Navigation Error", "Could not load halaman1 page: " + e.getMessage());
        }
    }

    private void registerDokterUser(String name, String username, String email, String password, String usia) {
        UserData user = new UserData(name, username, email, password, "Dokter", usia);

        ArrayList<UserData> users = userList.loadUser();
        if (users == null) {
            users = new ArrayList<>();
        }

        users.add(user);
        UserList.setLoginAccount(user);
        saveToXML(users);
        showStyledAlert(AlertType.INFORMATION, "Success", "Doctor account created successfully.");
        directToHalaman1();
    }

    private void registerPasienUser(String name, String username, String password, String usia) {
        UserData user = new UserData(name, username, "", password, "Pasien", usia);

        ArrayList<UserData> users = userList.loadUser();
        if (users == null) {
            users = new ArrayList<>();
        }

        users.add(user);
        UserList.setLoginAccount(user);
        
        saveToXML(users);
        showStyledAlert(AlertType.INFORMATION, "Success", "Patient account created successfully.");
        directToHalaman1();
    }

    private void saveToXML(ArrayList<UserData> users) {
        try {
            XStream xstream = new XStream(new StaxDriver());
            xstream.alias("user", UserData.class);
            String xml = xstream.toXML(users);
            
            try (FileOutputStream f = new FileOutputStream("users.xml")) {
                f.write(xml.getBytes("UTF-8"));
            }
        } catch (IOException e) {
            System.out.println("Error saving to XML: " + e.getMessage());
            showStyledAlert(AlertType.ERROR, "Error", "Failed to save user data.");
        }
    }
    private void setupStyling() {
        txtName.getStyleClass().add("text-field");
        txtUsername.getStyleClass().add("text-field");
        txtEmail.getStyleClass().add("text-field");
        tfUsia.getStyleClass().add("text-field");
        txtPassword.getStyleClass().add("password-field");
        txtConfirmPassword.getStyleClass().add("password-field");
        cbRole.getStyleClass().add("combo-box");
        registerButton.getStyleClass().add("register-button");
        loginHyperlink.getStyleClass().add("hyperlink");
        if (backButton != null) {
            backButton.getStyleClass().add("back-button");
        }
    }

    private void addHoverEffects() {
        registerButton.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), registerButton);
            scaleTransition.setToX(1.02);
            scaleTransition.setToY(1.02);
            scaleTransition.play();
        });

        registerButton.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), registerButton);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });

        if (backButton != null) {
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
        }

        loginHyperlink.setOnMouseEntered(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), loginHyperlink);
            scaleTransition.setToX(1.05);
            scaleTransition.setToY(1.05);
            scaleTransition.play();
        });

        loginHyperlink.setOnMouseExited(e -> {
            ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(100), loginHyperlink);
            scaleTransition.setToX(1.0);
            scaleTransition.setToY(1.0);
            scaleTransition.play();
        });
    }

    private void addFocusEffects() {
        TextField[] textFields = {txtName, txtUsername, txtEmail, tfUsia};
        PasswordField[] passwordFields = {txtPassword, txtConfirmPassword};

        for (TextField field : textFields) {
            field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    field.setStyle(field.getStyle() + "-fx-border-color: #f48c2a; -fx-border-width: 2;");
                } else {
                    field.setStyle(field.getStyle().replace("-fx-border-color: #f48c2a; -fx-border-width: 2;", ""));
                }
            });
        }

        for (PasswordField field : passwordFields) {
            field.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
                if (isNowFocused) {
                    field.setStyle(field.getStyle() + "-fx-border-color: #f48c2a; -fx-border-width: 2;");
                } else {
                    field.setStyle(field.getStyle().replace("-fx-border-color: #f48c2a; -fx-border-width: 2;", ""));
                }
            });
        }

        cbRole.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (isNowFocused) {
                cbRole.setStyle(cbRole.getStyle() + "-fx-border-color: #f48c2a; -fx-border-width: 2;");
            } else {
                cbRole.setStyle(cbRole.getStyle().replace("-fx-border-color: #f48c2a; -fx-border-width: 2;", ""));
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

    private void animateRoleChange() {
        ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(150), cbRole);
        scaleTransition.setToX(1.05);
        scaleTransition.setToY(1.05);
        scaleTransition.setAutoReverse(true);
        scaleTransition.setCycleCount(2);
        scaleTransition.play();
    }

    private void addPageTransition(Pane newPage) {
        FadeTransition fadeTransition = new FadeTransition(Duration.millis(300), newPage);
        fadeTransition.setFromValue(0.0);
        fadeTransition.setToValue(1.0);
        TranslateTransition slideTransition = new TranslateTransition(Duration.millis(300), newPage);
        slideTransition.setFromX(50);
        slideTransition.setToX(0);
        
        fadeTransition.play();
        slideTransition.play();
    }

    private void highlightEmptyFields() {
        if (txtName.getText().trim().isEmpty()) {
            highlightField(txtName);
        }
        if (txtUsername.getText().trim().isEmpty()) {
            highlightField(txtUsername);
        }
        if (txtPassword.getText().isEmpty()) {
            highlightField(txtPassword);
        }
        if (txtConfirmPassword.getText().isEmpty()) {
            highlightField(txtConfirmPassword);
        }
        if (tfUsia.getText().trim().isEmpty()) {
            highlightField(tfUsia);
        }
    }

    private void highlightField(TextField field) {
        field.getStyleClass().add("error-field");
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), field);
        shake.setFromX(0);
        shake.setToX(5);
        shake.setAutoReverse(true);
        shake.setCycleCount(4);
        shake.play();
    }

    private void highlightField(PasswordField field) {
        field.getStyleClass().add("error-field");
        TranslateTransition shake = new TranslateTransition(Duration.millis(50), field);
        shake.setFromX(0);
        shake.setToX(5);
        shake.setAutoReverse(true);
        shake.setCycleCount(4);
        shake.play();
    }

    private void removeErrorStyling() {
        txtName.getStyleClass().remove("error-field");
        txtUsername.getStyleClass().remove("error-field");
        txtEmail.getStyleClass().remove("error-field");
        tfUsia.getStyleClass().remove("error-field");
        txtPassword.getStyleClass().remove("error-field");
        txtConfirmPassword.getStyleClass().remove("error-field");
    }

    private void addSuccessStyling() {
        txtName.getStyleClass().add("success-field");
        txtUsername.getStyleClass().add("success-field");
        txtEmail.getStyleClass().add("success-field");
        tfUsia.getStyleClass().add("success-field");
        txtPassword.getStyleClass().add("success-field");
        txtConfirmPassword.getStyleClass().add("success-field");
    }
}