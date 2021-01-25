package View;

import Database.DBOperation;
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
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;

public class ModifyScheduleController
{
    private DBOperation db = new DBOperation();

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

    ArrayList<Integer> employeeIDs = new ArrayList<>();
    ArrayList<String> employeeNames = new ArrayList<>();
    String week[] = new String[7];

    public ModifyScheduleController() throws SQLException { }

    public void initialize() throws SQLException
    {
        getWeek();
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
                                useLastWeekSchedule(employeeIDs.get(row));
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
                            addShift(col-1, employeeIDs.get(row));
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
                            changeShift(col-1, employeeIDs.get(row));
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

    private ObservableList<Schedule> getDataFromScheduleAndAddToObservableList()
    {
        ObservableList<Schedule> scheduleData = FXCollections.observableArrayList();
        employeeIDs.clear();
        employeeNames.clear();

        try
        {
            employeeIDs = db.getEmployeeIDs();
            employeeNames = db.getEmployeeNames();

            for(int i = 0; i < employeeIDs.size(); i++)
            {
                scheduleData.add(db.getSchedule(employeeIDs.get(i), employeeNames.get(i), week));
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        return scheduleData;
    }

    public void getWeek()
    {
        if(week[0] != null)
        {
            return;
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
    }

    private void useLastWeekSchedule(int employeeID) throws ParseException, SQLException
    {
        for(int i = 0; i < 7; i++)
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
            c.setTime(sdf.parse(week[i]));
            c.add(Calendar.DAY_OF_MONTH, -7);
            String lastWeekDate = sdf.format(c.getTime());

            db.useLastWeekSchedule(employeeID, lastWeekDate, week[i]);
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

