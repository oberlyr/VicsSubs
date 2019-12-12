package View;

import Database.DBConnection;
import Main.Employee;
import javafx.event.EventHandler;
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

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.sql.*;

public class LoginController
{
    @FXML
    private TextField tfusername;
    @FXML
    private Button loginButton;
    @FXML
    private PasswordField pfpassword;

    private Employee employee;

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
                if (isValidCredentials(userName,password))
                {
                    tfusername.clear();
                    pfpassword.clear();

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
    }

    private boolean isValidCredentials(String userName, String password) throws SQLException
    {
        boolean userPassOk = false;

        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();

        PreparedStatement stmt = connection.prepareStatement("select * from employee where username = ? AND password = ?");

        stmt.setString(1, userName);
        stmt.setString(2, password);

        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next())
        {
            if(resultSet.getString("username")!=null && resultSet.getString("password")!=null)
            {
                userPassOk = true;
                boolean isAdmin = false;
                if(resultSet.getString("IsAdmin").equals("1"))
                    isAdmin = true;

                employee = new Employee(
                        resultSet.getString("Employee_ID"),
                        resultSet.getString("firstName"),
                        resultSet.getString("lastName"),
                        isAdmin);
            }
        }

        if(!userPassOk)
        {
            tfusername.clear();
            pfpassword.clear();

            userPassOk = false;
        }
        return userPassOk;
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
