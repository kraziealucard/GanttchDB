package com.example.GanttchDB;

import Model.Problem;
import Model.ProjectEntity;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TreeTableCell;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Objects;

/**
 * Class for callback editable Table Cell of StartDate and EndDate
 */
class TreeTableDP extends TreeTableCell<ProjectEntity, Date> {

    private DatePicker datePicker;

    @Override
    public void startEdit() {
        if ((getTableRow().getTreeItem().getValue() instanceof Problem)) {
            super.startEdit();
            createDatePicker();
            setText(null);
            setGraphic(datePicker);
        }
    }

    @Override
    public void cancelEdit() {
        super.cancelEdit();
        if (getDate()!=null)
        setText(Objects.requireNonNull(getDate()).toString());
        else setText("");
        setGraphic(null);
    }

    @Override
    public void updateItem(Date item, boolean empty) {
        super.updateItem(item, empty);
        if (empty) {
            setText(null);
            setGraphic(null);
        } else {
            if (isEditing()) {
                if (datePicker != null) {
                    datePicker.setValue(getDate());
                }
                setText(null);
                setGraphic(datePicker);
            } else {
                if (getDate()==null)
                {
                    setText("");
                }
                else {
                    setText(getDate().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)));
                }
                setGraphic(null);
            }
        }
    }

    private void createDatePicker() {
        datePicker = new DatePicker(getDate());
        datePicker.setMinWidth(this.getWidth() - this.getGraphicTextGap() * 2);
        datePicker.setOnAction((e) -> {
            if (datePicker.getValue()==null) commitEdit(null);
            else commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
            getTreeTableView().refresh();
        });
        datePicker.getEditor().textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String oldValue, String newValue) {
                if (Objects.equals(newValue, ""))
                {
                    datePicker.setValue(null);
                }
            }
        });
//            datePicker.focusedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
//                if (!newValue) {
//                    commitEdit(Date.from(datePicker.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant()));
//                }
//            });
    }

    private LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    private LocalDate getDate() {
        return getItem() == null ? null : Instant.ofEpochMilli(getItem().getTime()).atZone(ZoneId.systemDefault()).toLocalDate();
    }
}