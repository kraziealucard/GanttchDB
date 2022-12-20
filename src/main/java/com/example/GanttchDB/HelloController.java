package com.example.GanttchDB;

import DAO.DAOFactory;
import Model.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class HelloController implements Initializable {

    public TextField name;
    DAOFactory DAO;

    /** enter in program
     * @throws IOException no user
     */
    @FXML
    private void SignIn() throws IOException {
        int UserID;
        if(!name.getText().isEmpty()) {
            UserID = DAO.getUserDAO().getUser(name.getText());
            if(UserID!=-1){
                enterToGanttch(UserID);
                name.getScene().getWindow().hide();
            }
            else {
                UserID = DAO.getUserDAO().addUser(new User(-1,name.getText()));
                if(UserID!=-1){
                    enterToGanttch(UserID);
                    name.getScene().getWindow().hide();
                }
            }
        }
    }

    /** create new controller
     * @param user entered
     * @throws IOException no user
     */
    public void enterToGanttch(int user) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage(StageStyle.DECORATED);
        stage.setTitle("Диаграмма Ганта");
        stage.setScene(scene);
        MainController controller=fxmlLoader.getController();
        controller.init(user,DAO);
        stage.show();
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        DAO=DAOFactory.getDAOFactory(DAOFactory.H2);
    }
}