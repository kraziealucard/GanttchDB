package com.example.GanttchDB;

import Model.Problem;
import Model.ProjectEntity;
import Model.Status;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.TreeTableCell;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Class for callback editable Table Cell of category of duration
 */
class TreeTableCB extends TreeTableCell<ProjectEntity,String> {

    private ComboBox<String> comboBox;
    private ArrayList<Status> statuses;

    public TreeTableCB(){}

    public TreeTableCB(ArrayList<Status> statuses){
        this.statuses=statuses;
    }

    /**
     * Method for display comboBox
     */
    @Override
    public void startEdit() {
        if ( (getTableRow().getTreeItem().getValue() instanceof Problem) ) {
            super.startEdit();
            createComboBox();
            setText(null);
            setGraphic(comboBox);
        }
    }

    /**
     * Actions when undoing editing
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();

        setText(getTyp());
        setGraphic(null);
    }

    /**
     *
     * @param item The new item for the cell.
     * @param empty whether or not this cell represents data from the list. If it
     *        is empty, then it does not represent any domain data, but is a cell
     *        being used to render an "empty" row.
     */
    @Override
    public void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (comboBox != null) {
                    comboBox.setValue(getTyp());
                }
                setText(getTyp());
                setGraphic(comboBox);
            } else {
                setText(getTyp());
                setGraphic(null);
            }
        }
    }

    /**
     * Method for create comboBox and adding values
     */
    private void createComboBox() {
        comboBox = new ComboBox<>();
        comboBoxConverter(comboBox);
        ObservableList<String> items = FXCollections.observableArrayList();

        if (Objects.equals(getTableColumn().getText(), "Статус")) {
            items.add("");
            for (int i = 0; i < statuses.size(); i++) {
                items.add(statuses.get(i).getName());
            }
        }
        else {
            items.add("Дней");
            items.add("Месяцев");
            items.add("Лет");
        }
        comboBox.setItems(items);
        comboBox.valueProperty().set(getTyp());
        comboBox.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        comboBox.setOnAction((e) -> {
            commitEdit(comboBox.getSelectionModel().getSelectedItem());
            getTreeTableView().refresh();
        });
//            comboBox.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(comboBox.getSelectionModel().getSelectedItem());
//                }
//            });
    }

    /**
     * Method saving value on table
     * @param comboBox the selected item of which is stored in the table
     */
    private void comboBoxConverter(ComboBox<String> comboBox) {
        // Define rendering of the list of values in ComboBox drop down.
        comboBox.setCellFactory((c) -> {
            return new ListCell<String>() {
                @Override
                protected void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (item == null || empty) {
                        setText(null);
                    } else {
                        setText(item);
                    }
                }
            };
        });
    }

    private String getTyp() {
        return getItem() == null ? "" : getItem();
    }
}