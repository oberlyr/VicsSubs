package View;

import Database.DBOperation;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.sql.SQLException;

public class AddNewEmployeeController
{
    @FXML
    public TextField PassField;
    @FXML
    public TextField ConfirmField;
    @FXML
    public Button AddButton;
    @FXML
    Button CancelButton;
    @FXML
    public TextField FirstField;
    @FXML
    public TextField LastField;
    @FXML
    TextField UserField;
    @FXML
    Label Title;

    private String firstname;
    private String lastname;

    DBOperation db = new DBOperation();

    public AddNewEmployeeController() throws SQLException { }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) CancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void addUser() throws SQLException, IOException
    {
        String Firstname = FirstField.getText().trim();
        String Lastname = LastField.getText();
        String username = UserField.getText();
        String password = PassField.getText();
        String Confirmpassword = ConfirmField.getText();

        if(!firstname.equals(""))
            Firstname = firstname;
        if(!lastname.equals(""))
            Lastname = lastname;

        if(db.isNotEmployed(FirstField, LastField))
        {
            if(!password.equals(Confirmpassword))
            {
                Title.setText("Your passwords do not match! Please re-enter");
                PassField.clear();
                ConfirmField.clear();
            }
            else
            {
                db.addNewEmployee(username, password, Firstname, Lastname);

                Stage stage = (Stage) AddButton.getScene().getWindow();
                stage.close();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Add Employee");
                alert.setHeaderText("Thank You!");
                alert.setContentText("You have added this employee");
                alert.showAndWait();
            }
        }

        else
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are already employed!");
            alert.showAndWait();

            UserField.clear();
            PassField.clear();
            FirstField.clear();
            LastField.clear();
            ConfirmField.clear();
        }
    }

    public void onInit(String x, String y) throws SQLException {
        firstname = x;
        lastname = y;
        FirstField.setText(firstname);
        LastField.setText(lastname);

        if(!db.isNotEmployed(FirstField, LastField))
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Add User");
            alert.setHeaderText("Valid name needed");
            alert.setContentText("You are already employed!");
            alert.showAndWait();
        }
    }
}
