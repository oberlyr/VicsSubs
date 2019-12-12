package Database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;

import Main.Employee;

public class DBoperation {


    public static Statement logPunchIn(DBConnection database, Employee temp) throws SQLException {
        Connection connection = database.getConnection();
        Statement statement = connection.createStatement();

        Date date = new Date();
        String modifiedDate= new SimpleDateFormat("yyyy-MM-dd").format(date);

        String validate_punch = "Select ClockInTime from TimePunches where Employee_ID = '" + temp.getId() + "' AND CurrentDate = '"
                + modifiedDate + "'";

        System.out.println(validate_punch);

        //String update_command = "update employee set dbfirstName" + " = '" + temp.getFirstName() + "'," + "dblastName" + " = '" + temp.getLastName() +
        //		  "'" + ", dbstreet = '" + temp.getStreet() +"'" + ", dbpostalCode = " + temp.getPostalCode() + "," + "dbcity = '" + temp.getCity() + "'" + ", salary = '" + temp.getSalary() + "'" + " where dbpersonID = " + temp.getId() + ";";

        //statement.executeUpdate(update_command);
        return null;
    }

//    public static Statement addressBookInsert(DBConnection database, Person temp) throws SQLException {
//        Connection connection = database.getConnection();
//        Statement statement = connection.createStatement();
//
//
//        String insert_command = "insert into person values (" + "'" + temp.getId() + "'" + "," + "'" + temp.getFirstName() + "'" + "," + "'" + temp.getLastName() + "'" + "," + "'" + temp.getStreet() + "'" + "," + "'" + temp.getPostalCode() + "'" + "," + "'" + temp.getCity() + "'" + "," + "'" + temp.getSalary() + "'" + ");";
//
//        statement.executeUpdate(insert_command);
//        return statement;
//    }

    /**
     * delete record from address book ...
     */
//    public static Statement addressBookDeleteRecordPerson(DBConnection database, Connection connection, Person temp) throws SQLException {
//        connection = database.getConnection();
//        Statement statement = connection.createStatement();
//
//        String del_command = "delete from person where dbpersonID = " + temp.getId();
//        statement.executeUpdate(del_command);
//        return statement;
//    }
}