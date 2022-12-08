package DAO;

import Model.Project;
import java.util.ArrayList;

//Interface for implementing the DAO Project model
public interface IProjectDAO {
    void getProjectOfUser( ArrayList<Project> projects,long IDUser);
    boolean updateProject(Project project);
    int addProject(Project project);
    boolean deleteProject(long IDProject);
}
