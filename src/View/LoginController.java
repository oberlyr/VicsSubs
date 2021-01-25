package View;

import Database.DBOperation;
import Main.Employee;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.*;

public class LoginController
{
    private DBOperation db = new DBOperation();

    @FXML
    private TextField tfusername;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField pfpassword;

    private Employee employee;

    public LoginController() throws SQLException { }

    public void initialize()
    {
        tfusername.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                try {
                    loginButtonClicked();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        pfpassword.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.ENTER){
                try {
                    loginButtonClicked();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    public void loginButtonClicked() throws IOException
    {
        if (allFieldsEntered())
        {
            String userName = tfusername.getText().trim();
            String password = pfpassword.getText();

            try
            {
                if (db.isValidCredentials(userName,password))
                {
                    tfusername.clear();
                    pfpassword.clear();

                    employee = db.getEmployeeData(userName, password);

                    if(employee.getIsAdmin())
                    {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminWelcome.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        Stage stage = new Stage();
                        stage.setTitle("Welcome To Vic's!");
                        stage.setScene(new Scene(root1));
                        stage.show();
                    }
                    else
                    {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EmployeeWelcome.fxml"));
                        Parent root1 = (Parent) fxmlLoader.load();
                        EmployeeWelcomeController controller = fxmlLoader.getController();
                        controller.getEmployeeData(employee);
                        Stage stage = new Stage();
                        stage.setTitle("Welcome To Vic's, " + employee.getFirstName() + " " + employee.getLastName() + "!");
                        stage.setScene(new Scene(root1));
                        stage.show();
                    }
                }
                else
                {
                    tfusername.clear();
                    pfpassword.clear();

                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Failed");
                    alert.setHeaderText("Invalid Username or Password");
                    alert.setContentText("Please type in a correct username and password");
                    alert.showAndWait();
                }
            }
            catch (SQLException e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Not All Fields Filled Out");
            alert.setHeaderText("Not All Fields Filled Out");
            alert.setContentText("You must supply a username and password!");
            alert.showAndWait();
        }
    }

    private boolean allFieldsEntered()
    {
        boolean filledOut;

        if(tfusername.getText().trim().isEmpty() || pfpassword.getText().isEmpty())
            filledOut = false;
        else
            filledOut = true;

        return filledOut;
    }
}
