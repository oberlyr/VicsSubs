package View;

import Database.DBOperation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ClockInController
{
    DBOperation db = new DBOperation();
    private int employeeID;

    @FXML
    Button okButton;
    @FXML
    Label timeLabel;
    @FXML
    Label successText;

    public ClockInController() throws SQLException { }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    public void onInit(int id) throws SQLException
    {
        employeeID = id;
        logClockIn();
    }

    public static String getCurrentTime()
    {
        Date date = new Date();
        String strDateFormat = "hh:mm:ss a";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }

    public static String getCurrentDate()
    {
        Date date = new Date();
        String strDateFormat = "MM/dd/yyyy";
        DateFormat dateFormat = new SimpleDateFormat(strDateFormat);
        String formattedDate= dateFormat.format(date);
        return formattedDate;
    }

    @FXML
    public void logClockIn() throws SQLException
    {
        String currentTime = getCurrentTime();
        String currentDate = getCurrentDate();

        if(!db.isClockedIn(employeeID, currentDate))
        {
            db.clockIn(employeeID, currentDate, currentTime);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Welcome to your shift!");
            alert.setHeaderText("You have clocked in at " + currentTime + ".");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Already Clocked In");
            alert.setHeaderText("You have already clocked in");
            alert.showAndWait();
        }
    }
}