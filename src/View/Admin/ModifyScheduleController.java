package View;

import Database.DBConnection;
import Main.Employee;
import Main.Schedule;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ModifyScheduleController
{
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;

    @FXML
    Button doneButton;
    @FXML
    Button backWeekBtn;
    @FXML
    Button nextWeekBtn;
    @FXML
    Label weekLabel;
    @FXML
    TableView scheduleTable;
    @FXML
    TableColumn<Schedule, String> employeeCol;
    @FXML
    TableColumn<Schedule, String> sundayCol;
    @FXML
    TableColumn<Schedule, String> mondayCol;
    @FXML
    TableColumn<Schedule, String> tuesdayCol;
    @FXML
    TableColumn<Schedule, String> wednesdayCol;
    @FXML
    TableColumn<Schedule, String> thursdayCol;
    @FXML
    TableColumn<Schedule, String> fridayCol;
    @FXML
    TableColumn<Schedule, String> saturdayCol;

    ArrayList<Integer> EmployeeIDs = new ArrayList<>();
    ArrayList<String> EmployeeNames = new ArrayList<>();
    String week[] = new String[7];

    public void initialize() throws SQLException
    {
        showSchedule();
    }

    public void showSchedule() throws SQLException
    {
        scheduleTable.getItems().clear();

        employeeCol.setCellValueFactory(new PropertyValueFactory<Schedule, String>("employee"));
        sundayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("sunday"));
        mondayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("monday"));
        tuesdayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("tuesday"));
        wednesdayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("wednesday"));
        thursdayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("thursday"));
        fridayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("friday"));
        saturdayCol.setCellValueFactory(new PropertyValueFactory<Schedule,String>("saturday"));

        ObservableList<Schedule> data = getDataFromScheduleAndAddToObservableList();
        scheduleTable.getItems().addAll(data);

        scheduleTable.setEditable(true);
        scheduleTable.getSelectionModel().setCellSelectionEnabled(true);

        scheduleTable.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent click) {
                if (click.getClickCount() == 2) {
                    @SuppressWarnings("rawtypes")
                    TablePosition pos = (TablePosition) scheduleTable.getSelectionModel().getSelectedCells().get(0);
                    int row = pos.getRow();
                    int col = pos.getColumn();
                    @SuppressWarnings("rawtypes")
                    TableColumn column = pos.getTableColumn();
                    String val = column.getCellData(row).toString();
                    System.out.println("Selected Value, " + val + ", Column: " + col + ", Row: " + row);

                    if(col == 0)
                    {
                        try
                        {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will overwrite any shifts this employee has for the current week!", ButtonType.YES, ButtonType.CANCEL);
                            alert.setHeaderText("Use Last Week's Shifts for this Employee?");
                            alert.showAndWait();

                            if (alert.getResult() == ButtonType.YES)
                            {
                                useLastWeekSchedule(EmployeeIDs.get(row));
                            }
                        }
                        catch (SQLException | ParseException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else if(val.equals(""))
                    {
                        try
                        {
                            addShift(col-1, EmployeeIDs.get(row));
                        }
                        catch (IOException | SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                    else
                    {
                        try
                        {
                            changeShift(col-1, EmployeeIDs.get(row));
                        }
                        catch (IOException | SQLException e)
                        {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    private void addShift(int weekColumn, int employeeID) throws IOException, SQLException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AddShift.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        AddShiftController controller = fxmlLoader.getController();
        controller.onInit(week[weekColumn], employeeID, this);
        Stage stage = new Stage();
        stage.setTitle("Add New Shift");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private void changeShift(int weekColumn, int employeeID) throws IOException, SQLException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("EditShift.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        EditShiftController controller = fxmlLoader.getController();
        controller.onInit(week[weekColumn], employeeID, this);
        Stage stage = new Stage();
        stage.setTitle("Change Shift");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    private ObservableList<Schedule> getDataFromScheduleAndAddToObservableList(){
        ObservableList<Schedule> scheduleData = FXCollections.observableArrayList();
        EmployeeIDs.clear();
        EmployeeNames.clear();
        try {
            String sundayShift = "";
            String mondayShift = "";
            String tuesdayShift = "";
            String wednesdayShift = "";
            String thursdayShift = "";
            String fridayShift = "";
            String saturdayShift = "";
            String employee = "";
            String[] week = getWeek();

            connection = (Connection) database.getConnection();
            statement = (Statement) connection.createStatement();
            resultSet = statement.executeQuery("SELECT * FROM Employee WHERE IsAdmin = 0");

            while(resultSet.next())
            {
                EmployeeIDs.add(resultSet.getInt("Employee_Id"));
                EmployeeNames.add(resultSet.getString("FirstName") + " " + resultSet.getString("LastName"));
            }

            for(int i = 0; i < EmployeeIDs.size(); i++)
            {
                resultSet = statement.executeQuery("SELECT * from Schedule where Employee_Id = " + EmployeeIDs.get(i));
                employee = EmployeeNames.get(i);
                mondayShift = "";
                tuesdayShift = "";
                wednesdayShift = "";
                thursdayShift = "";
                fridayShift = "";
                saturdayShift = "";
                sundayShift = "";

                while(resultSet.next())
                {
                    String resultDate = resultSet.getString("ShiftDate");
                    String ShiftStartTime = resultSet.getString("ShiftStartTime");
                    String ShiftEndTime = resultSet.getString("ShiftEndTime");
                    String ShiftTime = "";
                    int startFirstColon = ShiftStartTime.indexOf(":");
                    int endFirstColon = ShiftEndTime.indexOf(":");
                    int startLength = ShiftStartTime.length();
                    int endLength = ShiftEndTime.length();

                    if(!ShiftStartTime.equals("REQUEST OFF"))
                    {
                        ShiftStartTime = ShiftStartTime.substring(0, startFirstColon + 3) + ShiftStartTime.substring(startLength - 3, startLength);
                        ShiftEndTime = ShiftEndTime.substring(0, endFirstColon + 3) + ShiftEndTime.substring(endLength - 3, endLength);
                        ShiftTime = ShiftStartTime + " - " + ShiftEndTime;
                    }
                    else
                        ShiftTime = ShiftStartTime;

                    if (resultDate.equals(week[0]))
                        mondayShift = ShiftTime;

                    else if (resultDate.equals(week[1]))
                        tuesdayShift = ShiftTime;

                    else if (resultDate.equals(week[2]))
                        wednesdayShift = ShiftTime;

                    else if (resultDate.equals(week[3]))
                        thursdayShift = ShiftTime;

                    else if (resultDate.equals(week[4]))
                        fridayShift = ShiftTime;

                    else if (resultDate.equals(week[5]))
                        saturdayShift = ShiftTime;

                    else if (resultDate.equals(week[6]))
                        sundayShift = ShiftTime;
                }
                scheduleData.add(new Schedule(employee, mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, saturdayShift, sundayShift));
            }
            connection.close();
            statement.close();
            resultSet.close();
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }
        return scheduleData;
    }

    public String[] getWeek()
    {
        if(week[0] != null)
        {
            return week;
        }

        LocalDate mostRecentMonday =
                LocalDate.now(ZoneId.of("America/Montreal"))
                        .with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        LocalDate current = mostRecentMonday;
        DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        for (int i = 0; i < 7; i++)
        {
            if (i != 0)
            {
                current = current.plusDays(1);
            }
            week[i] = current.format(mmddyyyy);
        }
        weekLabel.setText("Week of " + week[0] + " - " + week[6]);
        return week;
    }

    private void useLastWeekSchedule(int employeeID) throws ParseException, SQLException
    {
        for(int i = 0; i < 7; i++)
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            c.setTime(sdf.parse(week[i]));
            c.add(Calendar.DAY_OF_MONTH, -7);
            String lastWeek = sdf.format(c.getTime());

            DBConnection database = new DBConnection();
            Connection connection = database.getConnection();
            Statement statement = connection.createStatement();

            resultSet = statement.executeQuery("SELECT ShiftStartTime, ShiftEndTime FROM Schedule WHERE Employee_ID = "
                    + employeeID + " AND ShiftDate = '" + lastWeek + "';");
            try
            {
                resultSet.next();

                String startTime = resultSet.getString("ShiftStartTime");
                String endTime = resultSet.getString("ShiftEndTime");

                String str = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                        + employeeID + ", '" + week[i] + "', '" + startTime + "', '" + endTime + "');";

                statement.executeUpdate(str);
            }
            catch (Exception e)
            {
                //We don't care, just catching errors when they are off
            }
        }
        showSchedule();
    }

    @FXML
    public void goToLastWeek() throws ParseException, SQLException
    {
        for(int i = 0; i < 7; i++)
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            c.setTime(sdf.parse(week[i]));
            c.add(Calendar.DAY_OF_MONTH, -7);
            week[i] = sdf.format(c.getTime());
            weekLabel.setText("Week of " + week[0] + " - " + week[6]);
        }

        showSchedule();
    }

    @FXML
    public void goToNextWeek() throws ParseException, SQLException
    {
        for(int i = 0; i < 7; i++)
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            c.setTime(sdf.parse(week[i]));
            c.add(Calendar.DAY_OF_MONTH, 7);
            week[i] = sdf.format(c.getTime());
            weekLabel.setText("Week of " + week[0] + " - " + week[6]);
        }

        showSchedule();
    }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) doneButton.getScene().getWindow();
        stage.close();
    }
}

