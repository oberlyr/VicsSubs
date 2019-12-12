package Main;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Schedule
{
    private final StringProperty sunday;
    private final StringProperty monday;
    private final StringProperty tuesday;
    private final StringProperty wednesday;
    private final StringProperty thursday;
    private final StringProperty friday;
    private final StringProperty saturday;
    private final StringProperty employee;
    private final StringProperty hoursWorked;

    public Schedule(String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday)
    {
        this.sunday = new SimpleStringProperty(sunday);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
        this.employee = null;
        this.hoursWorked = null;
    }

    //For admin schedule that includes name
    public Schedule(String employeeName, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday)
    {
        this.sunday = new SimpleStringProperty(sunday);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
        this.employee = new SimpleStringProperty(employeeName);
        this.hoursWorked = null;
    }

    //For admin view hours worked
    public Schedule(String employeeName, String hoursWorked, String monday, String tuesday, String wednesday, String thursday, String friday, String saturday, String sunday)
    {
        this.sunday = new SimpleStringProperty(sunday);
        this.monday = new SimpleStringProperty(monday);
        this.tuesday = new SimpleStringProperty(tuesday);
        this.wednesday = new SimpleStringProperty(wednesday);
        this.thursday = new SimpleStringProperty(thursday);
        this.friday = new SimpleStringProperty(friday);
        this.saturday = new SimpleStringProperty(saturday);
        this.employee = new SimpleStringProperty(employeeName);
        this.hoursWorked = new SimpleStringProperty(hoursWorked);
    }

    public String getSunday() {
        return sunday.get();
    }

    public StringProperty sundayProperty() {
        return sunday;
    }

    public void setSunday(String sunday) {
        this.sunday.set(sunday);
    }

    public String getMonday() {
        return monday.get();
    }

    public StringProperty mondayProperty() {
        return monday;
    }

    public void setMonday(String monday) {
        this.monday.set(monday);
    }

    public String getTuesday() {
        return tuesday.get();
    }

    public StringProperty tuesdayProperty() {
        return tuesday;
    }

    public void setTuesday(String tuesday) {
        this.tuesday.set(tuesday);
    }

    public String getWednesday() {
        return wednesday.get();
    }

    public StringProperty wednesdayProperty() {
        return wednesday;
    }

    public void setWednesday(String wednesday) {
        this.wednesday.set(wednesday);
    }

    public String getThursday() {
        return thursday.get();
    }

    public StringProperty thursdayProperty() {
        return thursday;
    }

    public void setThursday(String thursday) {
        this.thursday.set(thursday);
    }

    public String getFriday() {
        return friday.get();
    }

    public StringProperty fridayProperty() {
        return friday;
    }

    public void setFriday(String friday) {
        this.friday.set(friday);
    }

    public String getSaturday() {
        return saturday.get();
    }

    public StringProperty saturdayProperty() {
        return saturday;
    }

    public void setSaturday(String saturday) {
        this.saturday.set(saturday);
    }

    public String getEmployee() {
        return employee.get();
    }

    public StringProperty employeeProperty() {
        return employee;
    }

    public void setEmployee(String employee) {
        this.employee.set(employee);
    }

    public String getHoursWorked() {
        return hoursWorked.get();
    }

    public StringProperty hoursWorkedProperty() {
        return hoursWorked;
    }

    public void setHoursWorked(String hoursWorked) {
        this.hoursWorked.set(hoursWorked);
    }

}
