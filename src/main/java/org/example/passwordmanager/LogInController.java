package org.example.passwordmanager;
import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

public class LogInController {

    @FXML
    Button signUpBTN, loginBTN;
    @FXML
    TextField emailTF, passwordTF;
    @FXML
    PasswordField passwordPF;
    boolean loginButtonPressed;
    int attempts = 0;

    /* Methods for input validation */

    private boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private boolean isMissingField(){
        return emailTF.getText().trim().isEmpty() ||
                passwordPF.getText().trim().isEmpty() ||
                passwordTF.getText().trim().isEmpty();
    }

    private Alert validateInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (!isValidEmailAddress(emailTF.getText().trim())) {
            alert.setTitle("Invalid e-mail address");
            alert.setContentText("Invalid e-mail or password. Please try again.");
            return alert;
        } else if (isMissingField()) {
            alert.setTitle("Missing fields");
            alert.setContentText("Please fill in all the fields.");
            passwordTF.clear();
            passwordPF.clear();
            return alert;
        }

        return null;
    }

    /* Method to prevent unlimited login attempts */

    private void handleLoginAttempts() {

        //If log in button was pressed, counts for one attempt
        if (loginButtonPressed) {
            attempts++;
            //Reset boolean
            loginButtonPressed = false;
        }

        //If log in attempts reached limit
        if (attempts >= 5){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Too many failed attempts. Try again after 30s.");
            alert.show();
            //Disable log in button
            loginBTN.setDisable(true);
            loginBTN.setStyle("-fx-background-color:grey");

            //Permit button to be used again after 30s
            PauseTransition pause = new PauseTransition(Duration.seconds(30));
            pause.setOnFinished(event -> {
                loginBTN.setDisable(false);
                loginBTN.setStyle("-fx-background-color:#0047AB");
            });
            pause.play();
        }
    }

    /* Log in button */

    @FXML
    void onClickLogIn() {
        loginButtonPressed = true;
        handleLoginAttempts();

        Alert validationAlert = validateInput();
        if (validationAlert == null) {
            Alert successAlert = new Alert(Alert.AlertType.CONFIRMATION);
            successAlert.setTitle("Logged in!");
            successAlert.setTitle("You're logged in.");
        } else {
            validationAlert.showAndWait();
        }
    }

    /* Sign up button */

    @FXML
    void onClickSignUp() {
        //Open registration page
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
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("An error occurred. Please try again.");
            System.out.println("Error: " + i);
        }
    }
}
