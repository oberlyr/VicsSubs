package View;

import Database.DBConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class EditEmployeeController
{
    @FXML
    TextField FirstField;
    @FXML
    TextField LastField;
    @FXML
    TextField UserField;
    @FXML
    TextField PassField;
    @FXML
    TextField ConfirmField;
    @FXML
    Label Title;
    @FXML
    Button CancelButton;
    @FXML
    Button UpdateButton;

    private String firstname;
    private String lastname;

    public void onInit(String x, String y) throws SQLException {
        firstname = x;
        lastname = y;
        if(!isNotEmployed())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are NOT employed!");
            alert.showAndWait();
        }
    }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }
    private boolean isNotEmployed() throws SQLException
    {
        boolean employed = false;
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String str = "SELECT * FROM Employee WHERE FirstName = '" + firstname + "' AND LastName = '"+lastname+"';";
        System.out.println(str);
        ResultSet resultSet = statement.executeQuery(str);

        if(resultSet.first())
        {
            employed = true;
        }
        return employed;
    }
    @FXML
    private void editUser() throws SQLException, IOException {
        String Firstname = FirstField.getText().trim();
        String Lastname = LastField.getText();
        String username = UserField.getText();
        String password = PassField.getText();
        String Confirmpassword = ConfirmField.getText();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        if(Firstname.equals(""))
        {Firstname = firstname; }

        if(Lastname.equals(""))
        {Lastname = lastname; }

        if(password.equals(""))
        {
            String str = "SELECT Password FROM Employee WHERE FirstName = '" + firstname +"';";
            System.out.println(str);
            ResultSet resultSet = statement.executeQuery(str);
            resultSet.next();
            password = resultSet.getString("Password");;
            Confirmpassword = resultSet.getString("Password");
        }

        if(username.equals(""))
        {
            String stat = "SELECT Username FROM Employee WHERE FirstName = '" + firstname +"';";
            System.out.println(stat);
            ResultSet reSet = statement.executeQuery(stat);
            reSet.next();
            username = reSet.getString("Username"); }

        if(isNotEmployed()) {

            if(!password.equals(Confirmpassword))
            {
                Title.setText("Your passwords do not match! please re-enter");
                PassField.clear();
                ConfirmField.clear();
            }
            else
                {
                String str = "UPDATE Employee " + "SET FirstName = '"+Firstname+ "', Lastname = '"+Lastname+"', Password = '"+password+"', Username = '"+ username +"' WHERE FirstName = '"+firstname+"'";
                System.out.println(str);
                statement.executeUpdate(str);
                Stage stage = (Stage) UpdateButton.getScene().getWindow();
                stage.close();}
                bringEdited();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are NOT employed!");
            alert.showAndWait();
            UserField.clear();
            PassField.clear();
            FirstField.clear();
            LastField.clear();
            ConfirmField.clear();
        }

    }
    @FXML
    public void bringEdited() throws IOException
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Edit Employee");
        alert.setHeaderText("Thank You!");
        alert.setContentText("You have edited this employee");
        alert.showAndWait();
    }
}
