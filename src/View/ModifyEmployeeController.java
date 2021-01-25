package View;


import Database.DBConnection;
import Main.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ModifyEmployeeController {
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    ArrayList<Integer> EmployeeIDs = new ArrayList<>();
    ArrayList<String> EmployeeLastNames = new ArrayList<>();
    ArrayList<String> EmployeeFirstNames = new ArrayList<>();

    @FXML
    Button CancelButton;
    @FXML
    TableView employeeTable;



    public void initialize() throws SQLException {
        fillTable();
    }

    public void fillTable() throws SQLException {
        Connection connection = database.getConnection();
        ObservableList<ObservableList> employeeInfo = FXCollections.observableArrayList();
        String sqlData = "SELECT FirstName, LastName from Employee where isAdmin = 0";
        ResultSet resultSet = connection.createStatement().executeQuery(sqlData);

        for (int i = 0; i < resultSet.getMetaData().getColumnCount(); i++) {
            final int j = i;
            TableColumn column = new TableColumn(resultSet.getMetaData().getColumnName(i + 1));
            column.setCellValueFactory
                    (new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

            employeeTable.getColumns().addAll(column);
        }

        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                row.add(resultSet.getString(i));
            }
            employeeInfo.add(row);
        }
        employeeTable.setItems(employeeInfo);
    }
    /*
    public void showEmployees() throws SQLException
    {
        employeeTable.getItems().clear();

        employeeIDCol.setCellValueFactory(new PropertyValueFactory<Employee, Integer>("employeeID"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("first name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<Employee, String>("last name"));
        adminCol.setCellValueFactory(new PropertyValueFactory<Employee, Boolean>("isadmin"));


        ObservableList<Employee> data = getDataFromEmployeesAndAddToObservableList();
        System.out.print(data.toString());
        employeeTable.getItems().addAll(data);
    }

    public ObservableList<Employee> getDataFromEmployeesAndAddToObservableList()
    {
        ObservableList<Employee> employeeData = FXCollections.observableArrayList();
        EmployeeIDs.clear();
        EmployeeFirstNames.clear();
        EmployeeLastNames.clear();
        try
        {
            String firstName ="";
            String lastName = "";
            connection = (Connection) database.getConnection();
            statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Employee WHERE IsAdmin = 0");

            while (resultSet.next())
            {
                EmployeeIDs.add(resultSet.getInt("Employee_Id"));
                EmployeeFirstNames.add(resultSet.getString("FirstName"));
                EmployeeLastNames.add(resultSet.getString("LastName"));
            }
            for(int i = 0; i < EmployeeIDs.size(); i++) {
                int employeeID = EmployeeIDs.get(i);
                firstName = EmployeeFirstNames.get(i);
                lastName = EmployeeLastNames.get(i);

                employeeData.add(new Employee(employeeID, firstName, lastName));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return employeeData;
    }

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
*/
        @FXML
        public void handleCloseButtonAction()
        {
            Stage stage = (Stage) CancelButton.getScene().getWindow();
            stage.close();
        }

    }

