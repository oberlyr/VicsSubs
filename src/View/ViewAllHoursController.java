package View;

import Database.DBConnection;
import Main.Schedule;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import java.util.ArrayList;
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

public class ViewAllHoursController {
    private DBConnection database = new DBConnection();
    private Connection connection;
    private Statement statement;
    private ResultSet employeeResultSet;
    @FXML
    Button doneButton;
    @FXML
    Button backWeekBtn;
    @FXML
    Button nextWeekBtn;
    @FXML
    Label weekLabel;
    @FXML
    TableView hoursWorkedTable;
    @FXML
    TableColumn<Schedule, String> employeeCol;
    @FXML
    TableColumn<Schedule, String> hoursWorkedCol;
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
    ArrayList<Integer> EmployeeIDs = new ArrayList<>();
    ArrayList<String> EmployeeNames = new ArrayList<>();
    String week[] = new String[7];

    public void initialize() throws SQLException, ParseException {
        getHoursWorked();
    }

    public void getHoursWorked() throws SQLException, ParseException {
        hoursWorkedTable.getItems().clear();
        connection = database.getConnection();
        Statement statement = connection.createStatement();

        this.employeeCol.setCellValueFactory(new PropertyValueFactory<Schedule, String>("employee"));
        this.hoursWorkedCol.setCellValueFactory(new PropertyValueFactory<Schedule, String>("hoursWorked"));
        this.mondayCol.setCellValueFactory(new PropertyValueFactory("monday"));
        this.tuesdayCol.setCellValueFactory(new PropertyValueFactory("tuesday"));
        this.wednesdayCol.setCellValueFactory(new PropertyValueFactory("wednesday"));
        this.thursdayCol.setCellValueFactory(new PropertyValueFactory("thursday"));
        this.fridayCol.setCellValueFactory(new PropertyValueFactory("friday"));
        this.saturdayCol.setCellValueFactory(new PropertyValueFactory("saturday"));
        this.sundayCol.setCellValueFactory(new PropertyValueFactory("sunday"));
        ObservableList<Schedule> hoursWorkedData = FXCollections.observableArrayList();
        EmployeeIDs.clear();
        EmployeeNames.clear();
        employeeResultSet = statement.executeQuery("SELECT * FROM Employee WHERE IsAdmin = 0");

        while(employeeResultSet.next())
        {
            EmployeeIDs.add(employeeResultSet.getInt("Employee_Id"));
            EmployeeNames.add(employeeResultSet.getString("FirstName") + " " + employeeResultSet.getString("LastName"));
        }

        String sundayShift = "";
        String mondayShift = "";
        String tuesdayShift = "";
        String wednesdayShift = "";
        String thursdayShift = "";
        String fridayShift = "";
        String saturdayShift = "";
        String employee = "";
        String[] week = getWeek();

        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        DateFormat hhmmaa = new SimpleDateFormat("hh:mm aa");
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm aa");

        for(int j = 0; j < EmployeeIDs.size(); j++) {
            employee = EmployeeNames.get(j);
            mondayShift = "";
            tuesdayShift = "";
            wednesdayShift = "";
            thursdayShift = "";
            fridayShift = "";
            saturdayShift = "";
            sundayShift = "";
            long hoursWorked = 0L;

            for (int i = 0; i < 7; ++i) {
                String current = week[i];
                String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + EmployeeIDs.get(j) + "' AND CurrentDate = '" + current + "';";
                ResultSet resultSet = statement.executeQuery(str);
                if (resultSet.first()) {
                    String cIn = hhmmaa.format(df.parse(resultSet.getString("ClockInTime")));
                    String cOut = hhmmaa.format(df.parse(resultSet.getString("ClockOutTime")));
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

            long totalSecs = hoursWorked / 1000L;
            long hours = totalSecs / 3600L;
            long mins = totalSecs / 60L % 60L;
            double decimal = round(((double) mins/60L), 2);
            String hrs = "" + ((double) hours + decimal);
            hoursWorkedData.add(new Schedule(employee, hrs, mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, saturdayShift, sundayShift));
        }

        this.hoursWorkedTable.getItems().addAll(hoursWorkedData);
        connection.close();
        statement.close();
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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

        getHoursWorked();
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

        getHoursWorked();
    }

    @FXML
    public void handleCloseButtonAction() throws SQLException, ParseException {
        Stage stage = (Stage)this.doneButton.getScene().getWindow();
        stage.close();
    }
}
