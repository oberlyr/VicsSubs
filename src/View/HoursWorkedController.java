package View;

import Database.DBConnection;
import Main.Schedule;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Calendar;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class HoursWorkedController
{
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet resultSet;
    private int employeeID;
    String[] week = new String[7];
    @FXML
    Button doneButton;
    @FXML
    Button btnlastWeek;
    @FXML
    Button btnnextWeek;
    @FXML
    Label hoursWorked;
    @FXML
    Label currentWeek;
    @FXML
    TableView hoursWorkedTable;
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
    @FXML
    TableColumn<Schedule, String> sundayCol;

    public void onInit(int id) throws SQLException, ParseException
    {
        this.employeeID = id;
        this.getWeek();
        hoursWorked.setText(getHoursWorked());
    }

    public String getHoursWorked() throws SQLException, ParseException
    {
        hoursWorkedTable.getItems().clear();

        DBConnection database = new DBConnection();
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        this.mondayCol.setCellValueFactory(new PropertyValueFactory("monday"));
        this.tuesdayCol.setCellValueFactory(new PropertyValueFactory("tuesday"));
        this.wednesdayCol.setCellValueFactory(new PropertyValueFactory("wednesday"));
        this.thursdayCol.setCellValueFactory(new PropertyValueFactory("thursday"));
        this.fridayCol.setCellValueFactory(new PropertyValueFactory("friday"));
        this.saturdayCol.setCellValueFactory(new PropertyValueFactory("saturday"));
        this.sundayCol.setCellValueFactory(new PropertyValueFactory("sunday"));

        ObservableList<Schedule> hoursWorkedData = FXCollections.observableArrayList();
        String sundayShift = "";
        String mondayShift = "";
        String tuesdayShift = "";
        String wednesdayShift = "";
        String thursdayShift = "";
        String fridayShift = "";
        String saturdayShift = "";

        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        DateFormat hhmmss = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");
        long hoursWorked = 0L;

        for(int i = 0; i < 7; ++i) {
            String current = week[i];
            String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + this.employeeID + "' AND CurrentDate = '" + current + "';";
            ResultSet resultSet = statement.executeQuery(str);
            if (resultSet.first()) {
                String cIn = hhmmss.format(df.parse(resultSet.getString("ClockInTime")));
                String cOut = hhmmss.format(df.parse(resultSet.getString("ClockOutTime")));
                Calendar clockInTime = Calendar.getInstance();
                Calendar clockOutTime = Calendar.getInstance();
                clockInTime.setTime(sdf.parse(cIn));
                clockOutTime.setTime(sdf.parse(cOut));
                String day = cIn + " - " + cOut;
                hoursWorked += clockOutTime.getTimeInMillis() - clockInTime.getTimeInMillis();
                if (i == 0) {
                    mondayShift = day;
                } else if (i == 1) {
                    tuesdayShift = day;
                } else if (i == 2) {
                    wednesdayShift = day;
                } else if (i == 3) {
                    thursdayShift = day;
                } else if (i == 4) {
                    fridayShift = day;
                } else if (i == 5) {
                    saturdayShift = day;
                } else if (i == 6) {
                    sundayShift = day;
                }
            }
        }

        hoursWorkedData.add(new Schedule(mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, saturdayShift, sundayShift));
        this.hoursWorkedTable.getItems().addAll(hoursWorkedData);
        long totalSecs = hoursWorked / 1000L;
        long hours = totalSecs / 3600L;
        long mins = totalSecs / 60L % 60L;
        String hrsworked = hours + " hours and " + mins + " minutes";
        connection.close();
        statement.close();
        return hrsworked;
    }
    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage)this.doneButton.getScene().getWindow();
        stage.close();
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
            currentWeek.setText("Week of " + week[0] + " - " + week[6]);
        }

        hoursWorked.setText(getHoursWorked());
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
            currentWeek.setText("Week of " + week[0] + " - " + week[6]);
        }

        hoursWorked.setText(getHoursWorked());
    }

    public String[] getWeek()
    {
        LocalDate mostRecentMonday = LocalDate.now(ZoneId.of("America/Montreal")).with(TemporalAdjusters.previous(DayOfWeek.MONDAY));
        DateTimeFormatter mmddyyyy = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        LocalDate current = mostRecentMonday;

        for(int i = 0; i < 7; i++)
        {
            if (i != 0)
            {
                current = current.plusDays(1L);
            }
            week[i] = current.format(mmddyyyy);
        }

        this.currentWeek.setText("Week of " + week[0] + " - " + week[6]);
        return week;
    }
}
