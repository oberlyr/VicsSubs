package Main;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * Model class for a Employee.
 **/

public class Employee
{
    private int id;
    private String firstName;
    private String lastName;
    private boolean isAdmin;

    private StringProperty FirstName;
    private StringProperty LastName;
    private IntegerProperty employeeID;

    /**
     * Default constructor.
     */
    //public Employee()
  //  {
    //    this(0, null, null, false);
   // }
    public Employee(int id, String firstName, String lastName)
    {
        this.FirstName = new SimpleStringProperty(firstName);
        this.LastName = new SimpleStringProperty(lastName);
        this.employeeID = new SimpleIntegerProperty(id);
    }

    public Employee(int id, String firstName, String lastName, boolean isAdmin)
    {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.isAdmin = isAdmin;
    }



    //For Admin Modify Employee


    public int getId()
    {
        return id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        this.firstName = firstName;
    }
    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        this.lastName = lastName;
    }

    public boolean getIsAdmin()
    {
        return isAdmin;
    }

    public void setIsAdmin(boolean isAdmin)
    {
        this.isAdmin = isAdmin;
    }

    public StringProperty FirstNameProperty(){return FirstName;}

}
