package View;

import Database.DBOperation;
import Main.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddShiftController
{
    DBOperation db = new DBOperation();

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

    private String shiftDate = "";
    private int employeeID = 0;
    private ModifyScheduleController Controller;

    public AddShiftController() throws SQLException { }

    public void onInit(String date, int employeeID, ModifyScheduleController controller) throws SQLException
    {
        shiftDate = date;
        this.employeeID = employeeID;
        Controller = controller;

        ObservableList<String> choices;
        choices = FXCollections.observableArrayList();
        choices.addAll("9:30:00 AM", "10:00:00 AM","10:30:00 AM","11:00:00 AM","11:30:00 AM","12:00:00 PM","12:30:00 PM","1:00:00 PM","1:30:00 PM","2:00:00 PM","2:30:00 PM","3:00:00 PM","3:30:00 PM","4:00:00 PM","4:30:00 PM","5:00:00 PM","5:30:00 PM","6:00:00 PM","6:30:00 PM","7:00:00 PM","7:30:00 PM","8:00:00 PM","8:30:00 PM","9:00:00 PM","9:30:00 PM","10:00:00 PM");
        cbstarttime.setItems(choices);
        cbendtime.setItems(choices);

        Employee employee = db.getEmployeeData(employeeID);

        lblemployee.setText("Add " + shiftDate + " Shift for " + employee.getFirstName() + " " + employee.getLastName());
    }

    @FXML
    private void onAddShift() throws SQLException, ParseException
    {
        String startTime = (String)cbstarttime.getValue();
        String endTime = (String)cbendtime.getValue();

        if(!validateHours())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Shift Add Failed");
            alert.setHeaderText("Shift Add Failed");
            alert.setContentText("Please Verify Times are Valid and Try Again");
            alert.showAndWait();

            Stage stage = (Stage) btnok.getScene().getWindow();
            stage.close();

            return;
        }

        db.addShift(employeeID, shiftDate, startTime, endTime);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Shift Added");
        alert.setHeaderText("Shift Added Successfully");
        alert.showAndWait();

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

    private boolean validateHours() throws ParseException
    {
        String startTime = (String)cbstarttime.getValue();
        String endTime = (String)cbendtime.getValue();

        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss a");
        Date start = sdf.parse(startTime);
        Date end = sdf.parse(endTime);

        if(end.before(start))
            return false;
        else
            return true;
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