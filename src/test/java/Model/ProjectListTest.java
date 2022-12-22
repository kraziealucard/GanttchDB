package Model;

import DAO.DAOFactory;
import javafx.embed.swing.JFXPanel;
import de.saxsys.javafx.test.JfxRunner;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

@RunWith(JfxRunner.class)
class ProjectListTest {


    private void deleteProjects(ProjectList projectList)
    {
        while (projectList.getProjects().size() != 0)
        {
            Project project=projectList.getProjects().get(0);
            projectList.removeProject(project);
        }
    }

    @Test
    void addProject() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);
        int diff=0;
        projectList.addProject("testAddProject "+(diff));
        diff=projectList.getProjects().size()-diff;
        Assert.assertEquals(1,diff);
        deleteProjects(projectList);
    }

    @Test
    void addProblemToProject() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());

        int diff=projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();

        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        diff=projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size()-diff;
        Assert.assertEquals(1,diff);
        deleteProjects(projectList);
    }

    @Test
    void updateProblemNumOfDuration() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);

        Long s= 10L;

        projectList.updateProblemNumOfDuration(problem,s);

        Assert.assertEquals(s,problem.getNumberOfDuration());
        deleteProjects(projectList);
    }

    @Test
    void updateTitle() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);

        String[] newNames=new String[2];
        newNames[0]="newNameProject";
        newNames[1]="newNameProblem";

        projectList.updateTitle(project,newNames[0]);
        projectList.updateTitle(problem,newNames[1]);

        String[] currentNames=new String[2];
        currentNames[0]=project.getTitle();
        currentNames[1]=problem.getTitle();
        Assert.assertEquals(newNames,currentNames);
        deleteProjects(projectList);
    }

    @Test
    void updateProblemCategoryOfDuration() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);

        String oldCategory=problem.getCategoryOfDuration();
        projectList.updateProblemCategoryOfDuration(problem,"Лет");
        Assert.assertNotEquals(problem.getCategoryOfDuration(),oldCategory);
        deleteProjects(projectList);
    }

    @Test
    void updateProblemStartDate() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date dateRepresentation = cal.getTime();

        Date oldDate=problem.getStartDay();
        projectList.updateProblemStartDate(problem, dateRepresentation);
        Date newDate=problem.getStartDay();

        Assert.assertNotEquals(oldDate,newDate);
        deleteProjects(projectList);
    }

    @Test
    void updateProblemEndDate() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 1988);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date dateRepresentation = cal.getTime();

        Date oldDate=problem.getEndDay();
        projectList.updateProblemEndDate(problem, dateRepresentation);
        Date newDate=problem.getEndDay();

        Assert.assertNotEquals(oldDate,newDate);
        deleteProjects(projectList);
    }

    @Test
    void removeProblemOfProject() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        String nameProblem="testAddProblem "+projectList.getProjects().get(projectList.getProjects().size()-1).getProblemList().size();
        projectList.addProblemToProject(projectList.getProjects().get(projectList.getProjects().size()-1),nameProblem);

        Project project=projectList.getProjects().get(projectList.getProjects().size()-1);
        Problem problem=project.getProblemList().get(project.getProblemList().size()-1);
        int oldSizeProjectList=project.getProblemList().size();

        projectList.removeProblemOfProject(project,problem);

        Assert.assertNotEquals(oldSizeProjectList,project.getProblemList().size());
        deleteProjects(projectList);
    }

    @Test
    void removeProject() {
        JFXPanel fxPanel = new JFXPanel();
        DAOFactory DAO= DAOFactory.getDAOFactory(DAOFactory.H2);
        ProjectList projectList=new ProjectList(1,DAO,new ArrayList<Status>());
        deleteProjects(projectList);

        projectList.addProject("testAddProject "+projectList.getProjects().size());
        int oldSizeProjectList=projectList.getProjects().size();

        projectList.removeProject(projectList.getProjects().get(0));

        Assert.assertNotEquals(oldSizeProjectList,projectList.getProjects().size());
        deleteProjects(projectList);
    }
}