package View;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class DeleteEmployeeController
{
    @FXML
    Button OKButton;

    @FXML
    public void handleCloseButtonAction()
    {
        Stage stage = (Stage) OKButton.getScene().getWindow();
        stage.close();
    }
}
