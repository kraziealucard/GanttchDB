package com.example.GanttchDB;

import Model.Problem;
import Model.ProjectEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeTableCell;
import java.util.Objects;

/**
 * Class for callback editable Table Cell of number of duration
 */
class TreeTableTF extends TreeTableCell<ProjectEntity, Long> {

    private TextField textField;

    /**
     * Method for display textField
     */
    @Override
    public void startEdit() {
        if ((getTableRow().getTreeItem().getValue() instanceof Problem)) {
            super.startEdit();
            createTextField();
            setText(null);
            setGraphic(textField);
            textField.selectAll();
        }
    }

    /**
     * Actions when undoing editing
     */
    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getItem()!=null)
        setText(getItem().toString());
        else setText("");
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
    public void updateItem(Long item, boolean empty) {
        super.updateItem(item, empty);

        if (empty) {

            if (item!=null)
            {
                setText(item.toString());
            }
            else
            {
                setText("");
            }
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (textField != null) {
                    textField.setText(getString());
//                        setGraphic(null);
                }
                setText(null);
                setGraphic(textField);
            } else {
                setText(getString());
                setGraphic(null);
            }
        }
    }

    /**
     * Method for create textField and setting a rule inability to enter anything other than numbers into a text field
     */
    private void createTextField() {
        textField = new TextField(getString());
        textField.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        textField.setOnAction((e) -> {
            if ((Objects.equals(textField.getText(), "")) || ( Objects.equals(textField.getText(), "0") ) )  {
                commitEdit(0L);
            }
            else
            {
                commitEdit(Long.parseLong(textField.getText()));
            }
            getTableRow().getTreeTableView().refresh();
        });

        textField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (!newValue.matches("\\d*") || newValue.length()>14) {
                    textField.setText(oldValue);
                }
            }
        });

        textField.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (!newValue) {
                if ((Objects.equals(textField.getText(), "")) || ( Objects.equals(textField.getText(), "0") ) )  {
                    commitEdit(0L);
                }
                else
                {
                    commitEdit(Long.parseLong(textField.getText()));
                }
                getTableRow().getTreeTableView().refresh();
            }
        });
    }

    private String getString() {
        return getItem() == null ? "" : getItem().toString();
    }
}