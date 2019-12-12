package View;

import Database.DBConnection;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewRequestsOffController {

    public void initialize() throws SQLException {
        fillTable();
        try {
            fillOutSelectedData();
        } catch (Exception e) {

        }
    }

    @FXML
    Button doneButton;

    @FXML
    Button approveButton;

    @FXML
    Button denyButton;

    @FXML
    TableView requestsOffTableView;

    @FXML
    TextField employeeIdTextField;

    @FXML
    TextField employeeTextField;

    @FXML
    TextField startDateTextField;

    @FXML
    TextField endDateTextField;

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }

    DBConnection dbConnection = new DBConnection();

    public void fillTable() throws SQLException {
        Connection connection = dbConnection.getConnection();
        ObservableList<ObservableList> requestOffInfo = FXCollections.observableArrayList();
        String sqlData = "SELECT Employee_ID, StartDate, EndDate from RequestedTimeOff";
        ResultSet resultSet = connection.createStatement().executeQuery(sqlData);

        for (int i = 0; i<resultSet.getMetaData().getColumnCount(); i++){
            final int j = i;
            TableColumn column = new TableColumn(resultSet.getMetaData().getColumnName(i+1));
            column.setCellValueFactory
                    (new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                        @Override
                        public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                            return new SimpleStringProperty(param.getValue().get(j).toString());
                        }
                    });

            requestsOffTableView.getColumns().addAll(column);
        }

        while (resultSet.next()) {
            ObservableList<String> row = FXCollections.observableArrayList();
            for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++){
                row.add(resultSet.getString(i));
            }
            requestOffInfo.add(row);
        }
        requestsOffTableView.setItems(requestOffInfo);
    }

    public void fillOutSelectedData() {
        requestsOffTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            Object selectedItems = requestsOffTableView.getSelectionModel().getSelectedItems().get(0);
            String employeeId = selectedItems.toString().split(",")[0].substring(1);
            employeeIdTextField.setText(employeeId);
            String startDate = selectedItems.toString().split(",")[1].substring(1);
            startDateTextField.setText(startDate);
            String endDate = selectedItems.toString().split(",")[2].substring(1);
            endDateTextField.setText(endDate.substring(0, endDate.length() - 1));
            try {
                String employeeName = getNameFromId(Integer.valueOf(employeeId));
                employeeTextField.setText(employeeName);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
    }

    public void removeRowFromTable(){
        Object selectedItems = requestsOffTableView.getSelectionModel().getSelectedItems().get(0);
        requestsOffTableView.getItems().remove(selectedItems);
    }

    @FXML
    public void deny() throws SQLException {
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String deleteRow = "DELETE FROM RequestedTimeOff WHERE Employee_ID = '" + employeeIdTextField.getText()
                + "' AND StartDate = '" + startDateTextField.getText() + "' AND EndDate = '"
                + endDateTextField.getText() + "';";
        int resultSet = statement.executeUpdate(deleteRow);
        removeRowFromTable();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Request Denied");
        alert.setHeaderText("Request Denied");
        alert.showAndWait();

        employeeTextField.setText("");
        endDateTextField.setText("");
        startDateTextField.setText("");
        employeeIdTextField.setText("");
    }

    @FXML
    public void approve() throws SQLException, ParseException {
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();
        String employeeID = employeeIdTextField.getText();
        String startDate = startDateTextField.getText();
        String endDate = endDateTextField.getText();

        String updateRow = "UPDATE RequestedTimeOff SET Approval = '1' WHERE Employee_ID = '" + employeeID
                + "' AND StartDate = '" + startDate + "' AND EndDate = '" + endDate + "';";
        statement.executeUpdate(updateRow);

        String firstDayInsert = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                + employeeID + ", '" + startDate + "', 'REQUEST OFF', '')";
        System.out.println(firstDayInsert);
        statement.executeUpdate(firstDayInsert);

        if(!startDate.equals(endDate))
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            c.setTime(sdf.parse(startDate));
            while(!sdf.format(c.getTime()).equals(endDate))
            {
                c.add(Calendar.DAY_OF_MONTH, 1);
                String date = sdf.format(c.getTime());

                String nextDayInsert = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                        + employeeID + ", '" + date + "', 'REQUEST OFF', '')";

                System.out.println(nextDayInsert);
                statement.executeUpdate(nextDayInsert);
            }
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Request Approved");
        alert.setHeaderText("Request Approved");
        alert.showAndWait();

        String deleteRow = "DELETE FROM RequestedTimeOff WHERE Employee_ID = '" + employeeIdTextField.getText()
                + "' AND StartDate = '" + startDateTextField.getText() + "' AND EndDate = '"
                + endDateTextField.getText() + "';";
        statement.executeUpdate(deleteRow);

        removeRowFromTable();

        employeeTextField.setText("");
        endDateTextField.setText("");
        startDateTextField.setText("");
        employeeIdTextField.setText("");
    }

    private String getNameFromId(int id) throws SQLException {
        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        String sql = "SELECT FirstName, LastName FROM Employee WHERE Employee_ID = " + id + ";";
        ResultSet resultSet = statement.executeQuery(sql);
        resultSet.next();
        String firstName = resultSet.getString(1);
        String lastName = resultSet.getString(2);
        String employeeFirstAndLast = firstName + " " + lastName;
        return employeeFirstAndLast;
    }

}
