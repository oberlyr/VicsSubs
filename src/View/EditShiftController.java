package View;

import Database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class EditShiftController
{
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @FXML
    Label lblemployee;
    @FXML
    ChoiceBox cbstarttime;
    @FXML
    ChoiceBox cbendtime;
    @FXML
    Button btnok;
    @FXML
    Button btncancel;
    @FXML
    Button btndelete;
    @FXML
    RadioButton LunchStart;
    @FXML
    RadioButton DinnerStart;
    @FXML
    RadioButton LunchEnd;
    @FXML
    RadioButton WkDyClose;
    @FXML
    RadioButton WkEdClose;
    @FXML
    ToggleGroup Start;
    @FXML
    ToggleGroup End;

    private String ShiftDate = "";
    private int EmployeeID = 0;
    private ModifyScheduleController Controller;

    public void onInit(String date, int employeeID, ModifyScheduleController controller) throws SQLException
    {
        ShiftDate = date;
        EmployeeID = employeeID;
        Controller = controller;

        ObservableList<String> choices;
        choices = FXCollections.observableArrayList();
        choices.addAll("9:30:00 AM", "10:00:00 AM","10:30:00 AM","11:00:00 AM","11:30:00 AM","12:00:00 PM","12:30:00 PM","1:00:00 PM","1:30:00 PM","2:00:00 PM","2:30:00 PM","3:00:00 PM","3:30:00 PM","4:00:00 PM","4:30:00 PM","5:00:00 PM","5:30:00 PM","6:00:00 PM","6:30:00 PM","7:00:00 PM","7:30:00 PM","8:00:00 PM","8:30:00 PM","9:00:00 PM","9:30:00 PM","10:00:00 PM");
        cbstarttime.setItems(choices);
        cbendtime.setItems(choices);

        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String str = "SELECT ShiftStartTime, ShiftEndTime FROM Schedule WHERE ShiftDate = '" + ShiftDate + "' AND Employee_ID = " + EmployeeID;
        ResultSet resultSet = statement.executeQuery(str);
        resultSet.next();

        cbstarttime.setValue(resultSet.getString("ShiftStartTime"));
        cbendtime.setValue(resultSet.getString("ShiftEndTime"));

        resultSet = statement.executeQuery("SELECT FirstName, LastName FROM Employee WHERE Employee_ID = " + EmployeeID);
        resultSet.next();

        lblemployee.setText("Alter " + ShiftDate + " Shift for " + resultSet.getString("FirstName") + " " + resultSet.getString("LastName"));
    }

    @FXML
    private void onEditShift() throws SQLException
    {
        //Add method to verify the time is valid
        String startTime = (String)cbstarttime.getValue();
        String endTime = (String)cbendtime.getValue();

        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String str = "UPDATE Schedule SET ShiftStartTime = '" + startTime + "', ShiftEndTime = '" + endTime +
                "' WHERE Employee_ID = " + EmployeeID + " AND ShiftDate = '" + ShiftDate + "';";
        System.out.println(str);

        try
        {
            statement.executeUpdate(str);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Shift Changed");
            alert.setHeaderText("Shift Changed Successfully");
            alert.showAndWait();
        }
        catch (SQLException e)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Shift Change Failed");
            alert.setHeaderText("Shift Change Failed");
            alert.setContentText("Please Verify Times are Valid and Try Again");
            alert.showAndWait();
        }

        Controller.showSchedule();

        Stage stage = (Stage) btnok.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onCancel()
    {
        Stage stage = (Stage) btncancel.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void onDeleteShift() throws SQLException
    {
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this shift?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Delete Shift Confirmation");
        alert.setHeaderText("Please confirm shift deletion");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES)
        {
            String deleteShift = "DELETE FROM Schedule WHERE Employee_ID = " + EmployeeID + " AND ShiftDate = '" + ShiftDate + "'";
            statement.executeUpdate(deleteShift);
            Controller.showSchedule();
            Stage stage = (Stage) btndelete.getScene().getWindow();
            stage.close();
        }
    }
    @FXML
    private void onLunchStart()
    {
        cbstarttime.setValue("11:00:00 AM");
    }

    @FXML
    private void onDinnerStart()
    {
        cbstarttime.setValue("3:00:00 PM");
    }

    @FXML
    private void onLunchEnd()
    {
        cbendtime.setValue("3:00:00 PM");
    }

    @FXML
    private void onWeekDayEnd()
    {
        cbendtime.setValue("8:00:00 PM");
    }

    @FXML
    private void onWeekendEnd()
    {
        cbendtime.setValue("9:30:00 PM");
    }
}
