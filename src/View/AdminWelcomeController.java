package View;


import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;


import java.io.IOException;

public class AdminWelcomeController
{
    @FXML
    Button LogoutButton;

    @FXML
    public void bringUpInputHoursScreen() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("InputHoursScreen.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Manually Input or Delete Hours");
        stage.setScene(new Scene(root1));
        stage.show();
    }

    @FXML
    public void bringUpModifyEmployee() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModifyEmployee.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Modifying Employee");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void bringUpViewRequestsOff() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewRequestsOff.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Employee Requests");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void bringUpModifySchedule() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ModifySchedule.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Modifying Schedule");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void bringUpViewAllHoursWorked() throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ViewAllHours.fxml"));
        Parent root1 = (Parent) fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("View Hours Worked");
        stage.setScene(new Scene(root1));
        stage.show();

    }

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) LogoutButton.getScene().getWindow();
        stage.close();
    }
}
