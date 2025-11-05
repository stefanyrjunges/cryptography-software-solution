package org.example.passwordmanager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.validator.routines.EmailValidator;
import java.io.IOException;
import java.util.Objects;
import javafx.scene.control.ToggleButton;

public class LogInController {

    @FXML
    Button signUpBTN, loginBTN;
    @FXML
    ToggleButton toggleButton;
    @FXML
    TextField emailTF, passwordTF;
    @FXML
    PasswordField passwordPF;
    @FXML
    ImageView viewHideIMG;
    boolean loginButtonPressed;
    int attempts = 0;
    Alert errorAlert = new Alert(Alert.AlertType.ERROR);

    @FXML
    private void initialize (){
        errorAlert.setHeaderText(null);
    }

    /*Methods for input validation - Stefany*/

    private boolean isValidEmailAddress(String email) {
        //Using Apache Commons Validator to ensure the data is valid
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private boolean isMissingField(){
        boolean emailEmpty = emailTF.getText().trim().isEmpty();
        boolean passwordEmpty = passwordPF.isVisible()
                ? passwordPF.getText().trim().isEmpty()
                : passwordTF.getText().trim().isEmpty();
        return emailEmpty || passwordEmpty;
    }

    private Alert validateInput() {
        //Ensuring data is valid and form is filled
        if (!isValidEmailAddress(emailTF.getText().trim())) {
            errorAlert.setTitle("Invalid e-mail address");
            errorAlert.setContentText("Invalid e-mail or password. Please try again.");
            //If user had too many failed attempts, don't display the errorAlert
            if (attempts < 5){
                return errorAlert;
            }
        } else if (isMissingField()) {
            errorAlert.setTitle("Missing fields");
            errorAlert.setContentText("Please fill in all the fields.");
            passwordTF.clear();
            passwordPF.clear();
            return errorAlert;
        }

        return null;
    }

    /*Method to prevent unlimited login attempts - Stefany*/

    private void handleLoginAttempts() {
        //If log in button was pressed, counts for one attempt
        if (loginButtonPressed) {
            attempts++;
            //Reset boolean
            loginButtonPressed = false;
        }

        //If log in attempts reached limit
        if (attempts >= 5){
            Alert limitAttemptsAlert = new Alert(Alert.AlertType.ERROR);
            limitAttemptsAlert.setHeaderText(null);
            limitAttemptsAlert.setTitle("Error");
            limitAttemptsAlert.setContentText("Too many failed attempts. Try again later.");
            limitAttemptsAlert.show();
            //Disable log in button
            loginBTN.setDisable(true);
            loginBTN.setStyle("-fx-background-color:grey");

            //Allow button to be used again after 30s
            PauseTransition pause = new PauseTransition(Duration.seconds(30));
            pause.setOnFinished(event -> {
                loginBTN.setDisable(false);
                loginBTN.setStyle("-fx-background-color:#0047AB");
                attempts = 0;
            });
            pause.play();
        }
    }

    /*Toggle button to hide/unhide password - Stefany*/

    @FXML
    void onClickToggleButton() {
        //Importing images
        Image eyeOpen = new Image(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/images/view.png")).toExternalForm());
        Image eyeClosed = new Image(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/images/hide.png")).toExternalForm());

        if (toggleButton.isSelected()) {
            //Managing fields visibility according to selection
            passwordTF.setText(passwordPF.getText());
            passwordTF.setVisible(true);
            passwordTF.setManaged(true);
            passwordPF.setVisible(false);
            passwordPF.setManaged(false);

            //Updating both text field and password field at the same time
            passwordTF.textProperty().addListener((obsVal, oldVal, newVal) -> passwordPF.setText(newVal));
            //Changing icon image
            viewHideIMG.setImage(eyeClosed);
        } else {
            passwordPF.setText(passwordTF.getText());
            passwordTF.setVisible(false);
            passwordTF.setManaged(false);
            passwordPF.setVisible(true);
            passwordPF.setManaged(true);
            viewHideIMG.setImage(eyeOpen);
        }
    }

    /*Log in button - Felipe*/

    //Helper to read visible password
    private String getPasswordInput() {
        return passwordPF.isVisible() ? passwordPF.getText() : passwordTF.getText();
    }

    @FXML
    void onClickLogIn() {
        //Storing pressed button for later use
        loginButtonPressed = true;
        //Calling method to check amount of log in attempts
        handleLoginAttempts();

        //Validating input and displaying a message according to result
        Alert validationAlert = validateInput();
        if (validationAlert == null) {
            String email = emailTF.getText().trim().toLowerCase();
            String input = getPasswordInput();

            String storedHash = AppContext.UserRepo.findHashByEmail(email);

            boolean ok = storedHash != null && PasswordHash.verify(input, storedHash);

            if (ok) {
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setHeaderText(null);
                successAlert.setTitle("Logged in!");
                successAlert.setContentText("You're logged in.");
                successAlert.showAndWait();
            } else {
                Alert fail = new Alert(Alert.AlertType.ERROR);
                fail.setHeaderText(null);
                fail.setTitle("Invalid credentials");
                fail.setContentText("Invalid e-mail or password. Please try again.");
                fail.showAndWait();
            }
        } else {
            validationAlert.showAndWait();
        }
    }

    /* Sign up button - Stefany*/

    @FXML
    void onClickSignUp() {
        //Open registration page when button is pressed
        try {
            Stage currentStage = (Stage) signUpBTN.getScene().getWindow();
            currentStage.close();

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("registration.fxml"));
            Parent root = fxmlLoader.load();

            Stage newStage = new Stage();
            newStage.setTitle("Register");
            newStage.setScene(new Scene(root));
            newStage.setResizable(false);
            newStage.show();
        } catch (IOException i) {
            errorAlert.setTitle("Error");
            errorAlert.setContentText("An error occurred. Please try again.");
            System.out.println("Error: " + i);
        }
    }

    @FXML
    private ToggleButton themeToggle;

    @FXML
    private void onToggleTheme() {
        Scene scene = themeToggle.getScene();
        if (scene == null) return;

        if (themeToggle.isSelected()) {
            // Switch to dark mode
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("dark-theme.css").toExternalForm());
            themeToggle.setText("☀ Light Mode");
        } else {
            // Switch back to light mode
            scene.getStylesheets().clear();
            scene.getStylesheets().add(getClass().getResource("light-theme.css").toExternalForm());
            themeToggle.setText("☾ Dark Mode");
        }
    }
}
