package View;

import Database.DBOperation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockOutController
{
    DBOperation db = new DBOperation();
    private int employeeID;

    @FXML
    Button okButton;
    @FXML
    Label timeLabel;
    @FXML
    Label successText;

    public ClockOutController() throws SQLException { }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void onInit(int id) throws SQLException
    {
        employeeID = id;
        logClockOut();
    }

    public static String getCurrentTime()
    {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        return dateFormat.format(date);
    }

    public static String getCurrentDate()
    {
        Date date = new Date();
        String strDateFormat = "MM/dd/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);

        return formattedDate;
    }

    public void logClockOut() throws SQLException
    {
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();

        if(!db.isClockedIn(employeeID, currentDate))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have not clocked in");
            alert.setHeaderText("You have not clocked in today. Please go clock in and then try again.");
            alert.showAndWait();
        }
        else if(db.isClockedOut(employeeID, currentDate))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("You have already clocked out");
            alert.setHeaderText("You have already been clocked out for the day.");
            alert.showAndWait();
        }
        else
        {
            db.clockOut(employeeID, currentDate, currentTime);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Have a nice day!");
            alert.setHeaderText("You have clocked out at " + currentTime + ".");
            alert.showAndWait();
        }
    }
}


