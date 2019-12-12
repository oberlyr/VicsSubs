package Database;

import java.sql.*;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Main.Employee;
import Main.Schedule;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class DBOperation
{
    DBConnection database;
    Connection connection;
    Statement statement;
    ResultSet resultSet;

    public DBOperation() throws SQLException
    {
        database = new DBConnection();
        connection = database.getConnection();
        statement = connection.createStatement();
    }

    private boolean intToBool(int num)
    {
        if(num == 1)
            return true;
        else
            return false;
    }

    public Employee getEmployeeData(int employeeID) throws SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM Employee WHERE Employee_ID = " + employeeID);
        resultSet.next();

        Employee employee = new Employee(employeeID,
                resultSet.getString("FirstName"),
                resultSet.getString("LastName"),
                intToBool(resultSet.getInt("IsAdmin")));

        return employee;
    }

    public Employee getEmployeeData(String userName, String password) throws SQLException
    {
        resultSet = statement.executeQuery("SELECT * FROM Employee WHERE Username = '" + userName + "' AND Password = '" + password + "';");
        resultSet.next();

        Employee employee = new Employee(resultSet.getInt("Employee_ID"),
                resultSet.getString("FirstName"),
                resultSet.getString("LastName"),
                intToBool(resultSet.getInt("IsAdmin")));

        return employee;
    }

    public boolean isValidCredentials(String userName, String password) throws SQLException
    {
        boolean validCredentials = false;

        PreparedStatement stmt = connection.prepareStatement("select * from Employee where username = ? AND password = ?");

        stmt.setString(1, userName);
        stmt.setString(2, password);

        ResultSet resultSet = stmt.executeQuery();

        while (resultSet.next())
        {
            if(resultSet.getString("username") != null && resultSet.getString("password") != null)
                validCredentials = true;
        }

        return validCredentials;
    }

    /**Admin **/

    /** Add Shift **/
    public void addShift(int employeeID, String shiftDate, String startTime, String endTime) throws SQLException
    {
        String str = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                + employeeID + ", '" + shiftDate + "', '" + startTime + "', '" + endTime + "');";
        statement.executeUpdate(str);
    }

    /** Edit Shift **/
    public String[] getCurrentShiftTimes(String shiftDate, int employeeID) throws SQLException
    {
        String[] shiftTimes = new String[2];

        String str = "SELECT ShiftStartTime, ShiftEndTime FROM Schedule WHERE ShiftDate = '" + shiftDate + "' AND Employee_ID = " + employeeID;
        resultSet = statement.executeQuery(str);
        resultSet.next();

        shiftTimes[0] = resultSet.getString("ShiftStartTime");
        shiftTimes[1] = resultSet.getString("ShiftEndTime");

        return shiftTimes;
    }

    public void updateShift(int employeeID, String startTime, String endTime, String shiftDate) throws SQLException
    {
        String str = "UPDATE Schedule SET ShiftStartTime = '" + startTime + "', ShiftEndTime = '" + endTime +
                "' WHERE Employee_ID = " + employeeID + " AND ShiftDate = '" + shiftDate + "';";
        statement.executeUpdate(str);
    }

    public void deleteShift(int employeeID, String shiftDate) throws SQLException
    {
        String str = "DELETE FROM Schedule WHERE Employee_ID = " + employeeID + " AND ShiftDate = '" + shiftDate + "';";
        statement.executeUpdate(str);
    }

    /** Add Employee **/
    public boolean isNotEmployed(TextField FirstField, TextField LastField) throws SQLException
    {
        boolean employed = false;
        String Firstname = FirstField.getText().trim();
        String Lastname = LastField.getText();

        String str = "SELECT * FROM Employee WHERE FirstName = '" + Firstname + "' AND LastName = '" + Lastname + "';";
        System.out.println(str);
        resultSet = statement.executeQuery(str);

        if(!resultSet.first())
        {
            employed = true;
        }
        return employed;
    }

    public void addNewEmployee(String username, String password, String firstName, String lastName) throws SQLException
    {
        String str = "INSERT INTO Employee (Username, Password, FirstName, LastName) VALUES (" + "'" + username + "', '" + password
                + "', '" + firstName + "', '" + lastName + "'" + ")";
        System.out.println(str);
        statement.executeUpdate(str);
    }

    /** Input Hours **/
    public void insertClockIn(int employeeID, String time, String date) throws SQLException
    {
        String inputHours = "Insert into TimePunches (Employee_ID, ClockInTime, CurrentDate) Values (" + employeeID + ",'" + time + "', '" + date + "')";
        statement.executeUpdate(inputHours);
    }

    public void insertClockOut(int employeeID, String time, String date) throws SQLException
    {
        String inputClockOut = "update TimePunches set ClockOutTime = '" + time + "' where Employee_ID =" + employeeID + " and CurrentDate = '" + date + "'";
        statement.executeUpdate(inputClockOut);
    }

    public void deletePunchesForDay(int employeeID, String date) throws SQLException
    {
        String deleteHours = "Delete from TimePunches where Employee_ID = " + employeeID + " and CurrentDate = '" + date + "'";
        statement.executeUpdate(deleteHours);
    }

    public boolean doesTheClockInExist(int employeeID, String date) throws SQLException
    {
        boolean doesTheClockInExist = false;
        String checkClockIn = "Select * from TimePunches where Employee_ID = " + employeeID + " and CurrentDate = '" + date + "'";
        resultSet = statement.executeQuery(checkClockIn);

        if(resultSet.first())
        {
            doesTheClockInExist = true;
        }

        return doesTheClockInExist;
    }

    public boolean doTheseHoursExist(int employeeID, String date) throws SQLException
    {
        boolean doTheseHoursExist = false;
        String hoursCheck = "Select * from TimePunches where Employee_ID = " + employeeID + " and CurrentDate = '" + date + "'";
        resultSet = statement.executeQuery(hoursCheck);

        if(resultSet.first())
        {
            doTheseHoursExist = true;
        }
        return doTheseHoursExist;
    }

    public boolean doesThisUsernameExist(String username) throws SQLException
    {
        boolean doesTheUsernameExist = false;
        String usernameCheck = "Select * from Employee where Username = '" + username + "'";
        resultSet = statement.executeQuery(usernameCheck);

        if(resultSet.first())
        {
            doesTheUsernameExist = true;
        }
        return doesTheUsernameExist;
    }

    public int getEmployeeID(String username) throws SQLException
    {
        int employeeID = -1;
        if(doesThisUsernameExist(username))
        {
            String getEmployeeID = "Select Employee_ID from Employee where Username = '" + username + "'";
            resultSet = statement.executeQuery(getEmployeeID);
            resultSet.next();
            employeeID = resultSet.getInt("Employee_ID");
        }
        return employeeID;
    }

    /** Employee **/

    /** Clock In **/
    public boolean isClockedIn(int employeeID, String currentDate) throws SQLException
    {
        boolean clockedIn = false;

        String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + employeeID + "' AND CurrentDate = '" + currentDate + "';";
        System.out.println(str);
        resultSet = statement.executeQuery(str);

        if(resultSet.first() != false)
            clockedIn = true;

        return clockedIn;
    }

    public void clockIn(int employeeID, String currentDate, String currentTime) throws SQLException
    {
        String str = "INSERT INTO TimePunches ( Employee_ID, ClockInTime, CurrentDate) Values (" + employeeID + ", '" + currentTime + "', '" + currentDate + "')";
        statement.executeUpdate(str);
    }

    /** Clock Out **/
    public boolean isClockedOut(int employeeID, String currentDate) throws SQLException
    {
        boolean clockedOut = false;

        String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + employeeID + "' AND CurrentDate = '" + currentDate + "'";
        resultSet = statement.executeQuery(str);
        resultSet.next();

        if(resultSet.getString("ClockOutTime") != null)
            clockedOut = true;

        return clockedOut;
    }

    public void clockOut(int employeeID, String currentDate, String currentTime) throws SQLException
    {
        String str = "update TimePunches set ClockOutTime = '" + currentTime + "' where Employee_ID =" + employeeID + " and CurrentDate = '" + currentDate + "'";
        statement.executeUpdate(str);
    }

    /** View Hours **/
    public String[] getHoursWorked(int employeeID, String currentDate) throws SQLException, ParseException
    {
        String[] shiftTimes = new String[2];
        DateFormat df = new SimpleDateFormat("hh:mm:ss aa");
        DateFormat hhmmss = new SimpleDateFormat("hh:mm aa");

        String str = "SELECT * FROM TimePunches WHERE Employee_ID = '" + employeeID + "' AND CurrentDate = '" + currentDate + "';";
        resultSet = statement.executeQuery(str);

        if(resultSet.first())
        {
            shiftTimes[0] = hhmmss.format(df.parse(resultSet.getString("ClockInTime")));
            shiftTimes[1] = hhmmss.format(df.parse(resultSet.getString("ClockOutTime")));
        }

        return shiftTimes;
    }

    /** Modify Schedule **/
    public ArrayList<Integer> getEmployeeIDs() throws SQLException
    {
        ArrayList<Integer> employeeIDs = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT Employee_ID FROM Employee WHERE IsAdmin = 0");

        while(resultSet.next())
            employeeIDs.add(resultSet.getInt("Employee_Id"));

        return employeeIDs;
    }

    public ArrayList<String> getEmployeeNames() throws SQLException
    {
        ArrayList<String> employeeNames = new ArrayList<>();
        resultSet = statement.executeQuery("SELECT FirstName, LastName FROM Employee WHERE IsAdmin = 0");

        while(resultSet.next())
            employeeNames.add(resultSet.getString("FirstName") + " " + resultSet.getString("LastName"));

        return employeeNames;
    }

    public Schedule getSchedule(int employeeID, String employeeName, String[] week) throws SQLException
    {
        String sundayShift = "";
        String mondayShift = "";
        String tuesdayShift = "";
        String wednesdayShift = "";
        String thursdayShift = "";
        String fridayShift = "";
        String saturdayShift = "";
        String employee = "";

        resultSet = statement.executeQuery("SELECT * from Schedule where Employee_Id = " + employeeID);

        employee = employeeName;

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
        return new Schedule(employee, mondayShift, tuesdayShift, wednesdayShift, thursdayShift, fridayShift, saturdayShift, sundayShift);
    }

    public void useLastWeekSchedule(int employeeID, String lastWeekDate, String currentWeekDate) throws SQLException
    {
        resultSet = statement.executeQuery("SELECT ShiftStartTime, ShiftEndTime FROM Schedule WHERE Employee_ID = "
                + employeeID + " AND ShiftDate = '" + lastWeekDate + "';");
        try
        {
            resultSet.next();

            String startTime = resultSet.getString("ShiftStartTime");
            String endTime = resultSet.getString("ShiftEndTime");

            String str = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                    + employeeID + ", '" + currentWeekDate + "', '" + startTime + "', '" + endTime + "');";

            statement.executeUpdate(str);
        }
        catch (Exception e)
        {
            //We don't care, just catching errors when they are off
        }
    }
}