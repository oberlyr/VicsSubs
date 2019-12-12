package View;

import Database.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AddNewEmployeeController
{
    @FXML
    public TextField PassField;
    @FXML
    public TextField ConfirmField;
    @FXML
    public Button AddButton;
    @FXML
    Button CancelButton;
    @FXML
    public TextField FirstField;
    @FXML
    public TextField LastField;
    @FXML
    TextField UserField;
    @FXML
    Label Title;

    private String firstname;
    private String lastname;



    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    private boolean isNotEmployed() throws SQLException
    {
        String Firstname = FirstField.getText().trim();
        String Lastname = LastField.getText();
        boolean employed = false;
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String str = "SELECT * FROM Employee WHERE FirstName = '" + Firstname + "' AND LastName = '"+Lastname+"';";
        System.out.println(str);
        ResultSet resultSet = statement.executeQuery(str);

        if(!resultSet.first())
        {
            employed = true;
        }
        return employed;
    }
    @FXML
    private void addUser() throws SQLException, IOException {
        String Firstname = FirstField.getText().trim();
        String Lastname = LastField.getText();
        String username = UserField.getText();
        String password = PassField.getText();
        String Confirmpassword = ConfirmField.getText();
        if(!firstname.equals(""))
        {
            Firstname = firstname;
        }
        if(!lastname.equals(""))
        {
            Lastname = lastname;
        }

        if(isNotEmployed()) {
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();
            String id = "SELECT COUNT(*) FROM Employee";
            ResultSet rst = statement.executeQuery(id);
            rst.next();
            int count = rst.getInt(1) + 1;
            if(!password.equals(Confirmpassword))
            {
                Title.setText("Your passwords do not match! please re-enter");
                PassField.clear();
                ConfirmField.clear();
            }
            else{
            String str = "INSERT INTO Employee (Username, Password, FirstName, LastName) VALUES (" + "'" + username + "', '" + password
                    + "', '" + Firstname + "', '" + Lastname + "'" + ")";
            System.out.println(str);
            statement.executeUpdate(str);
            Stage stage = (Stage) AddButton.getScene().getWindow();
            stage.close();}
            bringAdded();
        }
        else
        {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are already employed!");
            alert.showAndWait();
            UserField.clear();
            PassField.clear();
            FirstField.clear();
            LastField.clear();
            ConfirmField.clear();

        }

    }
    @FXML
    public void bringAdded() throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Add Employee");
        alert.setHeaderText("Thank You!");
        alert.setContentText("You have added this employee");
        alert.showAndWait();
    }
    public void onInit(String x, String y) throws SQLException {
        firstname = x;
        lastname = y;
        FirstField.setText(firstname);
        LastField.setText(lastname);
        if(!isNotEmployed())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are already employed!");
            alert.showAndWait();
        }
    }


}
