package com.example.GanttchDB;

import DAO.DAOFactory;
import Model.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;

import java.awt.*;
import java.util.ArrayList;
import java.util.Objects;

public class StatusView {
    public TableView<Status> StatusTable;
    public TableColumn<Status,String> NameCol;
    public TableColumn<Status, Color> ColorCol;
    long UserID;
    DAOFactory DAO;
    ArrayList<Status> statuses;
    public void init(long userID, DAOFactory DAO, ArrayList<Status> statuses)
    {
        setValueFactor();
        this.statuses=statuses;
        addTableItems();
        UserID=userID;
        this.DAO=DAO;
    }

    private void addTableItems()
    {
        ObservableList<Status> items=FXCollections.observableArrayList();
        StatusTable.setItems(items);
        for (Status status : statuses) {
            items.add(status.clone());
        }
    }

    @FXML
    private void saveData()
    {
        for (int i = 0; i < StatusTable.getItems().size()-1; i++) {
            for (int j = i+1; j <StatusTable.getItems().size() ; j++) {
                if (Objects.equals(StatusTable.getItems().get(i).getName(), StatusTable.getItems().get(j).getName())
                        || StatusTable.getItems().get(i).getName().isBlank())
                {
                    Alert alert=new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Ошибка");
                    alert.setHeaderText("Дубликаты статусов");
                    alert.setContentText("Есть дубликаты статусов, статусы не сохранены или есть статусы без имен");
                    alert.show();
                    StatusTable.getSelectionModel().clearSelection();
                    return;
                }
            }
        }


        for (int i = 0; i < StatusTable.getItems().size(); i++) {
            if (StatusTable.getItems().get(i).getID()==-1) {
                StatusTable.getItems().get(i).setID(DAO.getStatusDAO().addStatus(StatusTable.getItems().get(i)));
            }
            else DAO.getStatusDAO().updateStatus(StatusTable.getItems().get(i));
        }

        for (int i = 0; i < statuses.size(); i++) {
            boolean available = false;
            for (int j = 0; j < StatusTable.getItems().size(); j++) {
                if (statuses.get(i).getID() == StatusTable.getItems().get(j).getID()) available = true;
            }
            if (!available) {
                DAO.getStatusDAO().deleteStatus(statuses.get(i).getID());
                statuses.remove(i--);
            }
        }

        statuses.clear();
        statuses.addAll(StatusTable.getItems());

        Alert alert=new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Успех!");
        alert.setHeaderText("Статусы успешно сохранены");
        alert.setContentText("");
        alert.show();
        StatusTable.getSelectionModel().clearSelection();
    }

    private void setValueFactor() {
        this.NameCol.setCellValueFactory(new PropertyValueFactory<>("Name"));
        this.ColorCol.setCellValueFactory(new PropertyValueFactory<>("color"));

        Callback<TableColumn<Status,Color >, TableCell<Status,Color >> cellFactoryTF
                = (TableColumn<Status,Color > param) -> new ColorTableCell<Status>(ColorCol);

        ColorCol.setCellFactory(cellFactoryTF);
        NameCol.setCellFactory(TextFieldTableCell.forTableColumn());

        NameCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Status, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Status, String> event) {
                event.getRowValue().setName(event.getNewValue());
            }
        });

        ColorCol.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<Status, Color>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<Status, Color> event) {
                event.getRowValue().setColor(event.getNewValue());
            }
        });

        NameCol.setSortable(false);
        ColorCol.setSortable(false);
    }

    public void createNewStatus(ActionEvent actionEvent) {
        StatusTable.getItems().add(new Status(-1,UserID,Color.WHITE.getRGB()));
    }

    @FXML
    private void deleteStatus(){
        if ( StatusTable.getSelectionModel().getSelectedItem()==null )
        {
            Alert alert=new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Ошибка");
            alert.setHeaderText("Статус не был выбран");
            alert.setContentText("Вы не выбрали статус");
            alert.show();
            StatusTable.getSelectionModel().clearSelection();
            return;
        }

        StatusTable.getItems().remove(StatusTable.getSelectionModel().getSelectedIndex());
    }
}