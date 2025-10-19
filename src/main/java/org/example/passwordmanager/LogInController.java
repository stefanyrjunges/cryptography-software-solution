package org.example.passwordmanager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

import java.io.IOException;

public class LogInController {

    @FXML
    Button signUpBTN;
    @FXML
    TextField emailTF, passwordTF;
    @FXML
    PasswordField passwordPF;

    /* Methods for input validation */

    public boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    public boolean isMissingField(){
        return emailTF.getText().isEmpty() ||
                passwordPF.getText().isEmpty() ||
                passwordTF.getText().isEmpty();
    }

    public Alert validateInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (!isValidEmailAddress(emailTF.getText().trim())) {
            alert.setTitle("Invalid e-mail address");
            alert.setContentText("Invalid e-mail address. Please try again.");
            return alert;
        } else if (isMissingField()) {
            alert.setTitle("Missing fields");
            alert.setContentText("Please fill in all the fields.");
            return alert;
        }

        return null;
    }

    /* Log in button */

    @FXML
    private void onClickLogIn() {
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
    private void onClickSignUp() {
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
            alert.setContentText("Error: " + i);
        }
    }
}
