package com.example.GanttchDB;

import DAO.DAOFactory;
import Model.Problem;
import Model.Project;
import Model.ProjectEntity;
import Model.ProjectList;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTreeTableCell;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.util.Callback;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.util.*;

public class MainController {
    public TreeTableView<ProjectEntity> Table;
    public TreeTableColumn<ProjectEntity,String> nameCol;
    public TreeTableColumn<ProjectEntity,Long> durationCol;
    public TreeTableColumn<ProjectEntity,String> categoryDurationCol;
    public TreeTableColumn<ProjectEntity, Date> startCol;
    public TreeTableColumn<ProjectEntity,Date> endCol;
    public TextField nameProjectTF;
    public TextField nameProblemTF;
    public SplitPane splitP;
    private TreeItem<ProjectEntity> root;
    private ProjectList projectList;
    private GanttChart gc;

    public void init(long userID,DAOFactory DAO) {
        Table.columnResizePolicyProperty().setValue(TreeTableView.CONSTRAINED_RESIZE_POLICY);
        colsSetValueFactory();
        gc=new GanttChart(splitP);
        projectList=new ProjectList(userID,DAO);
        projectList.AddObs(gc);
        root=new TreeItem<>(null);
        Table.setRoot(root);
        root.setExpanded(true);
        Table.setShowRoot(false);

        initProjectAndProblem();
    }

    /**
     * Method for displaying data loaded from DB
     */
    private void initProjectAndProblem()
    {
        root.getChildren().clear();
        for (int i = 0; i < projectList.getProjects().size(); i++) {
            TreeItem<ProjectEntity> project=new TreeItem<>(projectList.getProjects().get(i));
            for (int j = 0; j < projectList.getProjects().get(i).getProblemList().size(); j++) {
                TreeItem<ProjectEntity> problem=new TreeItem<>(projectList.getProjects().get(i).getProblemList().get(j));
                project.getChildren().add(problem);
            }
            project.setExpanded(true);
            root.getChildren().add(project);
        }
        projectList.notifyObs();
        Table.getSelectionModel().clearSelection();
        nameProjectTF.clear();;
        nameProblemTF.clear();
        Table.refresh();
    }

    /**
     * Method for setCellFactory and setCellValueFactory for table column
     */
    private void colsSetValueFactory()
    {
        nameCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getValue().getTitle()));
        durationCol.setCellValueFactory(cellData->new SimpleObjectProperty<Long>(cellData.getValue().getValue().getNumberOfDuration()));
        categoryDurationCol.setCellValueFactory(cellData->new SimpleStringProperty(cellData.getValue().getValue().getCategoryOfDuration()));
        startCol.setCellValueFactory(cellData-> new SimpleObjectProperty<>(cellData.getValue().getValue().getStartDay()));
        endCol.setCellValueFactory(cellData->new SimpleObjectProperty<>(cellData.getValue().getValue().getEndDay()));

        nameCol.setCellFactory(TextFieldTreeTableCell.forTreeTableColumn());
        nameCol.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ProjectEntity, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ProjectEntity, String> event) {
                if(Objects.equals(event.getNewValue(), "")) event.getTreeTableView().refresh();
                else projectList.updateTitle(event.getRowValue().getValue(),event.getNewValue());
            }
        });

        Callback<TreeTableColumn<ProjectEntity,Long >, TreeTableCell<ProjectEntity,Long >> cellFactoryTF
                = (TreeTableColumn<ProjectEntity,Long > param) -> new TreeTableTF();
        durationCol.setCellFactory(cellFactoryTF);
        durationCol.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ProjectEntity, Long>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ProjectEntity, Long> event) {
                projectList.updateProblemNumOfDuration((Problem) event.getRowValue().getValue(),event.getNewValue());
            }
        });

        Callback<TreeTableColumn<ProjectEntity, String>, TreeTableCell<ProjectEntity, String>> cellFactoryCB
                = (TreeTableColumn<ProjectEntity, String> param) -> new TreeTableCB();
        categoryDurationCol.setCellFactory(cellFactoryCB);
        categoryDurationCol.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ProjectEntity, String>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ProjectEntity, String> event) {
                projectList.updateProblemCategoryOfDuration((Problem) event.getRowValue().getValue(), event.getNewValue());
            }
        });

        Callback<TreeTableColumn<ProjectEntity, Date>, TreeTableCell<ProjectEntity, Date>> cellFactoryDB
                = (TreeTableColumn<ProjectEntity, Date> param) -> new TreeTableDP();
        startCol.setCellFactory(cellFactoryDB);
        startCol.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ProjectEntity, Date>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ProjectEntity, Date> event) {
                projectList.updateProblemStartDate( (Problem) event.getRowValue().getValue(), event.getNewValue());
            }
        });

        endCol.setCellFactory(cellFactoryDB);
        endCol.setOnEditCommit(new EventHandler<TreeTableColumn.CellEditEvent<ProjectEntity, Date>>() {
            @Override
            public void handle(TreeTableColumn.CellEditEvent<ProjectEntity, Date> event) {
                projectList.updateProblemEndDate( (Problem) event.getRowValue().getValue(), event.getNewValue());
            }
        });
    }

    /**
     * Method for create and show Alert
     * @param head of Alert
     * @param content of Alert
     */
    private void showAlert(String head,String content)
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(head);
        alert.setContentText(content);
        alert.show();
        Table.getSelectionModel().clearSelection();
        nameProblemTF.clear();
        nameProjectTF.clear();
    }

    /**
     * Add project for user
     */
    public void addProjectBtn_Click() {
        if (nameProjectTF.getText().isEmpty())
        {
            showAlert("Нет названия","Вы не написали название для проекта");
            return;
        }

        for (int i = 0; i < projectList.getProjects().size(); i++) {
            if (Objects.equals(projectList.getProjects().get(i).getTitle(), nameProjectTF.getText()))
            {
                showAlert("Дубликат названия","Уже есть проект с данным названием");
                return;
            }
        }

        if(projectList.addProject(nameProjectTF.getText()));
        {
            initProjectAndProblem();
        }
    }

    /**
     * Add problem for project
     */
    public void addProblemBtn_Click() {
        if (Table.getSelectionModel().getSelectedItem()==null || !(Table.getSelectionModel().getSelectedItem().getValue() instanceof Project project))
        {
            showAlert("Проект не был выбран","Вы не выбрали проект");
            return;
        }

        if (nameProblemTF.getText().isEmpty())
        {
            showAlert("Не указано название","Вы не указали название для задачи");
            return;
        }

        for (int i = 0; i < project.getProblemList().size(); i++) {
            if (Objects.equals(project.getProblemList().get(i).getTitle(), nameProblemTF.getText()))
            {
                showAlert("Дубликат названия","В данном проекте уже есть задача с этим именем");
                return;
            }
        }

        if( projectList.addProblemToProject(project,nameProblemTF.getText()))
        {
            initProjectAndProblem();
        }
    }

    /**
     * Delete problem or project
     */
    public void deleteBtn_Click() {
        TreeItem<ProjectEntity> item=Table.getSelectionModel().getSelectedItem();
        if (item==null || item.getValue()==null)
        {
            showAlert("Ничего не выбрано","Вы ничего не выбрали для удаления");
            nameProjectTF.clear();
        }
        if (item.getValue() instanceof Project)
        {
            Table.getSelectionModel().getSelectedItem().getParent().getChildren().remove(item);
            projectList.removeProject((Project) item.getValue());
        }
        if (item.getValue() instanceof Problem problem)
        {
            Project p=(Project) item.getParent().getValue();
            projectList.removeProblemOfProject(p,(Problem)problem);
            Table.getSelectionModel().getSelectedItem().getParent().getChildren().remove(item);
        }
    }

    /**
     * Save gantt chart to png file
     */
    public void saveToPngClick() {
        FileChooser FC = new FileChooser();
        FC.getExtensionFilters().add(new FileChooser.ExtensionFilter("pngFile (*.png)", "*.png"));
        double position= splitP.getDividers().get(0).getPosition();
        splitP.setDividerPosition(0,0);
        WritableImage img = gc.getNode().snapshot(new SnapshotParameters(), null);
        splitP.setDividerPosition(0,position);
        File file = FC.showSaveDialog(null);
        if (file==null) return;
        try {
            ImageIO.write(SwingFXUtils.fromFXImage(img, null), "png", file);
        } catch (IOException exc) {
            exc.printStackTrace();
        }
    }
}
