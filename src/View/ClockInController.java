package View;

import Database.DBConnection;
import Main.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    private int employeeID;

    @FXML
    Button okButton;
    @FXML
    Label timeLabel;
    @FXML
    Label successText;

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) okButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void logClockIn() throws SQLException
    {
        if(isNotClockedIn())
        {
            System.out.println(getCurrentDate() + " " + getCurrentTime());
            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();

            String currentTime = getCurrentTime();
            String currentDate = getCurrentDate();

            String str = "INSERT INTO TimePunches ( Employee_ID, ClockInTime, CurrentDate) Values (" + employeeID + ", '" + currentTime + "', '" + currentDate + "')";
            statement.executeUpdate(str);
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

    public boolean isNotClockedIn() throws SQLException
    {
        boolean clockedIn = false;
        String currentDate = getCurrentDate();
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + employeeID + "' AND CurrentDate = '"+currentDate+"';";
        System.out.println(str);
        ResultSet resultSet = statement.executeQuery(str);

        if(resultSet.first() == false)
        {
            clockedIn = true;
        }
        return clockedIn;
    }
}
