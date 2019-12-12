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

public class ModifyEmployeeController
{
    /*private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet; **/

    private int employeeID;
    @FXML
    public TextField FirstNameField;
    @FXML
    public TextField LastNameField;
    @FXML
    Label LastNameLabel;
    @FXML
    Label FirstNameLabel;
    @FXML
    Button CancelButton;
    @FXML
    Button AddEmployeeButton;
    @FXML
    Button EditEmployeeButton;
    @FXML
    Button DeleteEmployeeButton;

    private boolean isNotEmployed() throws SQLException
    {
        String Firstname = FirstNameField.getText().trim();
        String Lastname = LastNameField.getText();
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
    public void bringUpAddEmployee() throws IOException, SQLException {
        if(!isNotEmployed())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are already employed!");
            alert.showAndWait();
            FirstNameField.clear();
            LastNameField.clear();
        }
        else{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddNewEmployee.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        AddNewEmployeeController control = fxmlLoader.getController();
        control.onInit(FirstNameField.getText(), LastNameField.getText());
        Stage stage = new Stage();
        stage.setTitle("Add New Employee");
        stage.setScene(new Scene(root1));
        stage.show();
        FirstNameField.clear();
        LastNameField.clear();
    }}

    @FXML
    public void bringUpEditEmployee() throws IOException, SQLException {
        if(isNotEmployed())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Edit User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are NOT employed!");
            alert.showAndWait();
            FirstNameField.clear();
            LastNameField.clear();
        }
        else{
        String firstname = FirstNameField.getText() +"";
        String lastname = LastNameField.getText() +"";
        if(firstname.equals("") || lastname.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have not entered a name");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("Please enter a name and try again");
            alert.showAndWait();
        }
        else
        {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditEmployee.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        EditEmployeeController control = fxmlLoader.getController();
        control.onInit(FirstNameField.getText(), LastNameField.getText());
        Stage stage = new Stage();
        stage.setTitle("Edit Existing Employee");
        stage.setScene(new Scene(root1));
        stage.show();
        FirstNameField.clear();
        LastNameField.clear();
    }}}

    @FXML
    public void bringUpDeleteConfirmation() throws IOException, SQLException {
        String firstname = FirstNameField.getText() +"";
        String lastname = LastNameField.getText() +"";
        if(firstname.equals("") || lastname.equals(""))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have not entered a name");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("Please enter a name and try again");
            alert.showAndWait();
            FirstNameField.clear();
            LastNameField.clear();
        }
        else if(isNotEmployed())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Delete User");
            alert.setHeaderText("This user is not employed");
            alert.setContentText("You are NOT employed!");
            alert.showAndWait();
            FirstNameField.clear();
            LastNameField.clear();
        }
        else
        {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("DeleteEmployeeConfirmation.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        DeleteEmployeeConfirmationController control = fxmlLoader.getController();
        control.onInit(FirstNameField.getText(), LastNameField.getText());
        Stage stage = new Stage();
        stage.setTitle("Delete an Employee");
        stage.setScene(new Scene(root1));
        stage.show();
        FirstNameField.clear();
        LastNameField.clear();

   }}

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

}
