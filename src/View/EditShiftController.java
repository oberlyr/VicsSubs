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

public class EditShiftController
{
    private DBOperation db = new DBOperation();

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

    private String shiftDate = "";
    private int employeeID = 0;
    private ModifyScheduleController Controller;

    public EditShiftController() throws SQLException { }

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

        String[] shiftTimes = db.getCurrentShiftTimes(shiftDate, this.employeeID);

        cbstarttime.setValue(shiftTimes[0]);
        cbendtime.setValue(shiftTimes[1]);

        Employee employee = db.getEmployeeData(employeeID);

        lblemployee.setText("Alter " + shiftDate + " Shift for " + employee.getFirstName() + " " + employee.getLastName());
    }

    @FXML
    private void onEditShift() throws SQLException, ParseException
    {
        String startTime = (String)cbstarttime.getValue();
        String endTime = (String)cbendtime.getValue();

        if(validateHours())
        {
            db.updateShift(employeeID, startTime, endTime, shiftDate);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Shift Changed");
            alert.setHeaderText("Shift Changed Successfully");
            alert.showAndWait();

            Controller.showSchedule();

            Stage stage = (Stage) btnok.getScene().getWindow();
            stage.close();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Shift Change Failed");
            alert.setHeaderText("Shift Change Failed");
            alert.setContentText("Please Verify Times are Valid and Try Again");
            alert.showAndWait();
        }
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this shift?", ButtonType.YES, ButtonType.CANCEL);
        alert.setTitle("Delete Shift Confirmation");
        alert.setHeaderText("Please confirm shift deletion");
        alert.showAndWait();

        if (alert.getResult() == ButtonType.YES)
        {
            db.deleteShift(employeeID, shiftDate);
            Controller.showSchedule();
            Stage stage = (Stage) btndelete.getScene().getWindow();
            stage.close();
        }
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
    { cbstarttime.setValue("11:00:00 AM"); }

    @FXML
    private void onDinnerStart()
    { cbstarttime.setValue("3:00:00 PM"); }

    @FXML
    private void onLunchEnd()
    { cbendtime.setValue("3:00:00 PM"); }

    @FXML
    private void onWeekDayEnd()
    { cbendtime.setValue("8:00:00 PM"); }

    @FXML
    private void onWeekendEnd()
    { cbendtime.setValue("9:30:00 PM"); }
}
