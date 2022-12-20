package DAO;

import Model.Problem;
import Model.Project;
import Model.Status;

import java.util.ArrayList;

//Interface for implementing the DAO Problem model
public interface IProblemDAO {
    void getProblemsOfProject(Project project, ArrayList<Status> statuses);
    boolean updateProblem(Problem problem);
    int addProblem(Problem problem);
    boolean deleteProblem(long IDProblem);
}
