package Database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import Main.Employee;
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

    /**Admin **/

    /** Add Shift **/
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

    public void addShift(int employeeID, String shiftDate, String startTime, String endTime) throws SQLException
    {
        String str = "INSERT INTO Schedule (Employee_ID, ShiftDate, ShiftStartTime, ShiftEndTime) VALUES ("
                + employeeID + ", '" + shiftDate + "', '" + startTime + "', '" + endTime + "');";
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
}