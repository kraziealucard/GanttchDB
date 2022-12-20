package com.example.GanttchDB;

import javafx.scene.control.*;

import java.awt.*;

public class ColorTableCell<T> extends TableCell<T, Color> {
    private final ColorPicker colorPicker;

    public ColorTableCell(TableColumn<T, Color> column) {
        this.colorPicker = new ColorPicker();
        this.colorPicker.editableProperty().bind(column.editableProperty());
        this.colorPicker.disableProperty().bind(column.editableProperty().not());
        this.colorPicker.setOnShowing(event -> {
            final TableView<T> tableView = getTableView();
            tableView.getSelectionModel().select(getTableRow().getIndex());
            tableView.edit(tableView.getSelectionModel().getSelectedIndex(), column);
        });
        this.colorPicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(isEditing()) {
                commitEdit(convertAwtColor(newValue));
            }
        });
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
    }

    @Override
    protected void updateItem(Color item, boolean empty) {
        super.updateItem(item, empty);

        setText(null);
        if(empty) {
            setGraphic(null);
        } else {
            this.colorPicker.setValue(convertSceneColor(item));
            this.setGraphic(this.colorPicker);
        }
    }

    private javafx.scene.paint.Color convertSceneColor(Color item)
    {
        int r = item.getRed();
        int g = item.getGreen();
        int b = item.getBlue();
        int a = item.getAlpha();
        double opacity = a / 255.0 ;
        return javafx.scene.paint.Color.rgb(r, g, b, opacity);
    }

    private Color convertAwtColor(javafx.scene.paint.Color item)
    {
        return new java.awt.Color((float) item.getRed(),
            (float) item.getGreen(),
            (float) item.getBlue(),
            (float) item.getOpacity());
    }
}
