package View;

import Database.DBConnection;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;

public class RequestOffController {

    @FXML
    Button cancelButton;

    @FXML
    DatePicker startDatePicker;

    @FXML
    DatePicker endDatePicker;

    private int employeeID;

    @FXML
    public void handleCloseButtonAction() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    public void setEmployeeID(int id)
    {
        employeeID = id;
    }

    @FXML
    public void requestOff() throws SQLException
    {
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String startDate = startDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));
        String endDate = endDatePicker.getValue().format(DateTimeFormatter.ofPattern("MM/dd/yyyy"));

        Date firstDate = Date.valueOf(startDatePicker.getValue());
        Date secondDate = Date.valueOf(endDatePicker.getValue());
        if (secondDate.after(firstDate) || firstDate.equals(secondDate)) {
            String str = "INSERT INTO RequestedTimeOff (Employee_ID, StartDate, EndDate) VALUES " +
                    "(" + employeeID + ", '" + startDate + "', '" + endDate + "')";
            System.out.println("Requested off: " + startDate + "-" + endDate);
            statement.executeUpdate(str);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success!");
            alert.setHeaderText("Request is Sent");
            alert.setContentText("Requested off: " + startDate + "-" + endDate);
            alert.showAndWait();
            Stage stage = (Stage) cancelButton.getScene().getWindow();
            stage.close();
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Bad Dates!");
            alert.setContentText("The start date cannot be after end date!");
            alert.showAndWait();
        }
    }

}
