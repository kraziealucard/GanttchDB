package DAO;

import Model.Problem;
import Model.Project;

//Interface for implementing the DAO Problem model
public interface IProblemDAO {
    void getProblemsOfProject(Project project);
    boolean updateProblem(Problem problem);
    int addProblem(Problem problem);
    boolean deleteProblem(long IDProblem);
}
