package View;

import Database.DBConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import javax.xml.transform.Result;
import java.sql.*;
import java.time.format.DateTimeFormatter;

public class InputHoursScreenController
{
    private DBConnection database = new DBConnection();
    private Connection connection = database.getConnection();
    private Statement statement = connection.createStatement();

    @FXML
    DatePicker datePicker;
    @FXML
    TextField UsernameField;
    @FXML
    ChoiceBox TimePicker;
    @FXML
    Button CancelButton;

    private String username = "";
    private String date = "";
    private String time = "";

    public InputHoursScreenController() throws SQLException
    {
        //TODO
    }
    public void initialize()
    {
        ObservableList<String> choices;
        choices = FXCollections.observableArrayList();
        choices.addAll("9:30:00 AM", "10:00:00 AM","10:30:00 AM","11:00:00 AM","11:30:00 AM","12:00:00 PM","12:30:00 PM","1:00:00 PM","1:30:00 PM","2:00:00 PM","2:30:00 PM","3:00:00 PM","3:30:00 PM","4:00:00 PM","4:30:00 PM","5:00:00 PM","5:30:00 PM","6:00:00 PM","6:30:00 PM","7:00:00 PM","7:30:00 PM","8:00:00 PM","8:30:00 PM","9:00:00 PM","9:30:00 PM","10:00:00 PM");
        TimePicker.setItems(choices);
    }

    @FXML
    public void bringUpClockIn() throws SQLException
    {
        if(datePicker.getValue() != null)
            date = datePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        time = "" + TimePicker.getValue();
        username = UsernameField.getText();
        if(username.equals("") || date.equals("") || time.equals("null"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing required information");
            alert.setHeaderText("Missing required information");
            alert.setContentText("Please input all information");
            alert.showAndWait();
        }

        else if(doesThisUsernameExist() && !doesTheClockInExist())
        {
            String inputHours = "Insert into TimePunches ( Employee_ID, ClockInTime, CurrentDate) Values (" + getEmployeeID() + ",'" + time + "', '" + date + "')";
            statement.executeUpdate(inputHours);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hours inputted successfully");
            alert.setHeaderText("You have inputted hours for " + UsernameField.getText() + " successfully");
            alert.showAndWait();
        }
        else if(!doesThisUsernameExist())
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText("Invalid Username");
            alert.setContentText("Please type in a correct username");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Clock in exists already");
            alert.setHeaderText("Clock in exists already");
            alert.setContentText("A clock in for " + username + " already exists on " + date);
            alert.showAndWait();
        }
    }

    @FXML
    public void bringUpClockOut() throws SQLException
    {
        if(datePicker.getValue() != null)
            date = datePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        time = "" + TimePicker.getValue();
        username = UsernameField.getText();
        if(username.equals("") || date.equals("") || time.equals("null"))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Missing required information");
            alert.setHeaderText("Missing required information");
            alert.setContentText("Please input all information");
            alert.showAndWait();
        }
        else {
            boolean doesThisClockInExist = doesTheClockInExist();
            if (doesThisClockInExist && doesThisUsernameExist()) {
                String inputClockOut = "update TimePunches set ClockOutTime = '" + time + "' where Employee_ID =" + getEmployeeID() + " and CurrentDate = '" + date + "'";
                statement.executeUpdate(inputClockOut);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Clock Out successful");
                alert.setHeaderText("You have clocked out " + username + " on " + date + " successfully");
                alert.showAndWait();
            } else if (!doesThisClockInExist) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("ClockIn on this date does not exist");
                alert.setContentText("Please type in a correct date or enter a clock in on this date");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Invalid Username");
                alert.setHeaderText("Invalid Username");
                alert.setContentText("Please type in a correct username");
                alert.showAndWait();
            }
        }
    }

    @FXML
    public void bringUpDeleteHours() throws SQLException
    {
        if(datePicker.getValue() != null)
            date = datePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        username = UsernameField.getText();
        boolean doTheHoursExist = doTheseHoursExist();
        if(doTheHoursExist && doesThisUsernameExist())
        {
            String deleteHours = "Delete from TimePunches where Employee_ID = " + getEmployeeID() + " and CurrentDate = '" + date + "'";
            System.out.println(deleteHours);
            statement.executeUpdate(deleteHours);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Hours deleted successfully");
            alert.setHeaderText("You have deleted hours for " + UsernameField.getText() + " successfully");
            alert.showAndWait();
        }
        else if(!doTheHoursExist)
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Hours do not exist");
            alert.setHeaderText("Invalid Hours");
            alert.setContentText("There are no hours on this date for the specified user");
            alert.showAndWait();
        }
        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Invalid Username");
            alert.setHeaderText("Invalid Username");
            alert.setContentText("Please type in a correct username");
            alert.showAndWait();
        }
    }

    @FXML
    public boolean doesTheClockInExist() throws SQLException
    {
        boolean doesTheClockInExist = true;
        String checkClockIn = "Select * from TimePunches where Employee_ID = " + getEmployeeID() + " and CurrentDate = '" + date + "'";
        ResultSet resultSet = statement.executeQuery(checkClockIn);
        if(!resultSet.first())
        {
            doesTheClockInExist = false;
        }

        return doesTheClockInExist;
    }
    @FXML
    public boolean doTheseHoursExist() throws SQLException
    {
        boolean doTheseHoursExist = true;
        String hoursCheck = "Select * from TimePunches where Employee_ID = " + getEmployeeID() + " and CurrentDate = '" + date + "'";
        ResultSet resultSet = statement.executeQuery(hoursCheck);
        if(!resultSet.first())
        {
            doTheseHoursExist = false;
        }
        return doTheseHoursExist;
    }

    public boolean doesThisUsernameExist() throws SQLException
    {
        boolean doesTheUsernameExist = true;
        String usernameCheck = "Select * from Employee where Username = '" + username + "'";
        ResultSet resultSet = statement.executeQuery(usernameCheck);

        if(!resultSet.first())
        {
            doesTheUsernameExist = false;
        }
        return doesTheUsernameExist;
    }
    public int getEmployeeID() throws SQLException {
        int employeeID = -1;
        if(doesThisUsernameExist())
        {
            String getEmployeeID = "Select Employee_ID from Employee where Username = '" + username + "'";
            ResultSet resultSet = statement.executeQuery(getEmployeeID);
            resultSet.next();
            employeeID = resultSet.getInt("Employee_ID");
        }
        return employeeID;
    }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

}
