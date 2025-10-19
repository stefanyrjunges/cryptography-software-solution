package org.example.passwordmanager;
import java.io.IOException;
import java.security.SecureRandom;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.beans.value.ChangeListener;
import java.util.Objects;
import javafx.stage.Stage;
import org.apache.commons.validator.routines.EmailValidator;

public class RegisterController {

    @FXML
    private TextField emailTF, passwordTF, confirmPasswordTF;
    @FXML
    private PasswordField passwordPF, confirmPasswordPF;
    @FXML
    Label charactersLBL, upplowLBL, numLBL, symLBL, strengthLBL;
    @FXML
    ImageView viewHideIMG, viewHideIMG2;
    @FXML
    ToggleButton toggleButton, toggleButton2;
    @FXML
    ProgressBar strengthBar;

    @FXML
    private void initialize() {
        /* Progress Bar listener */
        //Tracking text changes to update the progress bar
        ChangeListener<String> strengthListener = (_, _, newVal) -> updateStrengthForText(newVal);
        if (passwordTF != null) passwordTF.textProperty().addListener(strengthListener);
        if (passwordPF != null) passwordPF.textProperty().addListener(strengthListener);

        /* Password Requirements listener */
        //Adding a listener to the Password Field to keep track of requirements
        passwordPF.textProperty().addListener((_, _, newText) -> {
            boolean hasLength = newText.length() >= 12;
            boolean hasUpper = newText.matches(".*[A-Z].*");
            boolean hasLower = newText.matches(".*[a-z].*");
            boolean hasNumber = newText.matches(".*\\d.*");
            boolean hasSymbol = newText.matches(".*[^a-zA-Z0-9].*");

            //Changing labels' style as the requirements are fulfilled
            charactersLBL.setStyle(hasLength ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
            upplowLBL.setStyle(hasLower && hasUpper ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
            numLBL.setStyle(hasNumber ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
            symLBL.setStyle(hasSymbol ? "-fx-text-fill: green;" : "-fx-text-fill: red;");
        });
    }

    /* Methods for generating a strong password */

    @FXML
    void useStrongPassword() {
        //Filling the fields with the random generated password
        String generatedPassword = generateStrongPassword();
        passwordPF.setText(generatedPassword);
        passwordTF.setText(generatedPassword);
        confirmPasswordTF.setText(generatedPassword);
        confirmPasswordPF.setText(generatedPassword);
    }

    private String generateStrongPassword() {
        //Using cryptography to generate a random password
        SecureRandom random = new SecureRandom();

        //Storing possible characters to be used in the password
        String uppercaseChar = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        String lowercaseChar = "abcdefghijklmnopqrstuvwxyz";
        String numbersChar = "0123456789";
        String symbolsChar = "!@#$%^&*()-_=+[]{}|;:,.<>?";
        String allChars = uppercaseChar + lowercaseChar + numbersChar + symbolsChar;

        //Creating a new password by selecting random characters
        StringBuilder password = new StringBuilder();
        //Ensuring the password has at least one of each character
        password.append(uppercaseChar.charAt(random.nextInt(uppercaseChar.length())));
        password.append(lowercaseChar.charAt(random.nextInt(lowercaseChar.length())));
        password.append(numbersChar.charAt(random.nextInt(numbersChar.length())));
        password.append(symbolsChar.charAt(random.nextInt(symbolsChar.length())));

        //Generating remaining characters
        for (int i = 4; i < 12; i++) {
            password.append(allChars.charAt(random.nextInt(allChars.length())));
        }

        //Calling method to shuffle the characters
        return shuffleString(password.toString(), random);
    }

    private String shuffleString(String randomPassword, SecureRandom random) {
        //Creating an array of characters from the random generated password
        char[] chars = randomPassword.toCharArray();
        //Selecting random indexes and swapping them
        for (int i = 0; i < chars.length; i++) {
            int randomIndex = random.nextInt(chars.length);
            char temp = chars[i];
            chars[i] = chars[randomIndex];
            chars[randomIndex] = temp;
        }

        return new String(chars);
    }

    private double calculateEntropy(String password) {
        //Password contains 62 possible characters
        int passwordSize = 62;
        //Calculating entropy using entropy = password length × log₂(charset size)
        return password.length() * (Math.log(passwordSize) / Math.log(2));
    }

    private void updateStrengthForText(String password) {
        //Using entropy calculation to update the progress bar
        double entropy = calculateEntropy(password);
        updateProgressBar(entropy);
    }

    private void updateProgressBar(double entropy) {
        //Make changes based on password strength
        if (entropy < 40) {
            strengthLBL.setText("Weak");
            strengthBar.setProgress(0.25F);
            strengthBar.setStyle("-fx-accent: red;");
        } else if (entropy < 70) {
            strengthLBL.setText("Medium");
            strengthBar.setProgress(0.50F);
            strengthBar.setStyle("-fx-accent: orange;");
        } else {
            strengthLBL.setText("Strong");
            strengthBar.setProgress(1F);
            strengthBar.setStyle("-fx-accent: green;");
        }
    }

    /* Methods to hide/unhide the password */

    @FXML
    void handleToggle(ActionEvent event) {
        //Storing which toggle button has been selected
        ToggleButton toggleButtonSource = (ToggleButton) event.getSource();

        //Using different fields and images according to the selected toggle button
        if (toggleButtonSource == toggleButton) {
            toggleButton(passwordPF, passwordTF, viewHideIMG, toggleButton.isSelected());
        } else if (toggleButtonSource == toggleButton2) {
            toggleButton(confirmPasswordPF, confirmPasswordTF, viewHideIMG2, toggleButton2.isSelected());
        }
    }

    private void toggleButton(PasswordField pf, TextField tf, ImageView showHideIcon, boolean isSelected) {
        //Importing images
        Image eyeOpen = new Image(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/images/view.png")).toExternalForm());
        Image eyeClosed = new Image(Objects.requireNonNull(getClass().getResource("/org/example/passwordmanager/images/hide.png")).toExternalForm());

        if (isSelected) {
            //Managing fields visibility according to selection
            tf.setText(pf.getText());
            tf.setVisible(true);
            tf.setManaged(true);
            pf.setVisible(false);
            pf.setManaged(false);

            //Updating both text field and password field at the same time
            tf.textProperty().addListener((_, _, newVal) -> pf.setText(newVal));
            //Changing icon image
            showHideIcon.setImage(eyeClosed);
        } else {
            pf.setText(tf.getText());
            tf.setVisible(false);
            tf.setManaged(false);
            pf.setVisible(true);
            pf.setManaged(true);

            showHideIcon.setImage(eyeOpen);
        }
    }

    /* Methods for input validation */

    private boolean isValidEmailAddress(String email) {
        EmailValidator validator = EmailValidator.getInstance();
        return validator.isValid(email);
    }

    private boolean isMissingField() {
        return emailTF.getText().trim().isEmpty() ||
                passwordPF.getText().isEmpty() ||
                passwordTF.getText().isEmpty() ||
                confirmPasswordPF.getText().isEmpty() ||
                confirmPasswordTF.getText().isEmpty();
    }

    private boolean isStrongPassword() {
        return strengthLBL.getText().equalsIgnoreCase("strong");
    }

    private boolean isMatchPassword() {
        String password = passwordPF.isVisible() ? passwordPF.getText() : passwordTF.getText();
        String confirm = confirmPasswordPF.isVisible() ? confirmPasswordPF.getText() : confirmPasswordTF.getText();
        return password.equals(confirm);
    }

    private Alert validateInput() {
        Alert alert = new Alert(Alert.AlertType.ERROR);

        if (!isValidEmailAddress(emailTF.getText().trim())) {
            alert.setTitle("Invalid e-mail or password");
            alert.setContentText("Invalid e-mail or password. Please try again.");
            return alert;
        } else if (isMissingField()) {
            alert.setTitle("Missing fields");
            alert.setContentText("Please fill in all the fields.");
            return alert;
        } else if (!isStrongPassword()) {
            alert.setTitle("Weak password");
            alert.setContentText("Your password is weak. Please choose another or generate a random one.");
            return alert;
        } else if (!isMatchPassword()) {
            alert.setTitle("Mismatching passwords");
            alert.setContentText("Your passwords don't match.");
            passwordPF.clear();
            confirmPasswordPF.clear();
            passwordTF.clear();
            confirmPasswordTF.clear();
            return alert;
        }

        return null;
    }

    /* Sign up button */

    @FXML
    void onClickSignUp(ActionEvent event) {
        Alert validationAlert = validateInput();

        if (validationAlert == null) {
            Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
            successAlert.setTitle("Signed up!");
            successAlert.setContentText("You're all set!");
            successAlert.showAndWait();

            try {
                //Loading log in page again
                Stage currentStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                currentStage.close();

                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("login.fxml"));
                Parent root = fxmlLoader.load();

                Stage newStage = new Stage();
                newStage.setTitle("Log In");
                newStage.setScene(new Scene(root));
                newStage.setResizable(false);
                newStage.show();
            } catch (IOException i) {
                successAlert.setContentText("An error occurred. Please try again");
                System.out.println("Error: " + i);
                successAlert.showAndWait();
            }
        } else {
            validationAlert.showAndWait();
        }
    }

}
