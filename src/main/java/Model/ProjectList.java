package Model;

import DAO.DAOFactory;
import com.example.GanttchDB.IObserver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

    public class ProjectList implements IObserved {

    ArrayList<Project> projects;
    ArrayList<IObserver> observers;
    DAOFactory DAO;
    long UserID;
    public ProjectList(long userID,DAOFactory dao)
    {
        this.DAO=dao;
        this.UserID=userID;
        projects=new ArrayList<>();
        observers=new ArrayList<>();
        loadData();
    }

    private void loadData()
    {
        DAO.getProjectDAO().getProjectOfUser(projects,UserID);
        for (int i = 0; i < projects.size(); i++) {
            DAO.getProblemDAO().getProblemsOfProject(projects.get(i));
            for (int j = 0; j < projects.get(i).getProblemList().size(); j++) {
                calculateDuration(projects.get(i).getProblemList().get(j));
            }
        }
    }

    public ArrayList<Project> getProjects() {
        return projects;
    }
    public boolean addProject(String projectName) {
        Project temp=new Project(-1,projectName,UserID);
        long id=DAO.getProjectDAO().addProject(temp);
        if (id!=-1) {
            temp=new Project(id,projectName,UserID);
            projects.add(temp);
            notifyObs();
            return true;
        }
        return false;
    }
    public boolean addProblemToProject(Project project,String nameOfProblem) {
        Problem temp=new Problem(-1,project.getID(),nameOfProblem);
        long id=DAO.getProblemDAO().addProblem(temp);
        if (id!=-1) {
            temp=new Problem(id,project.getID(),nameOfProblem);
            project.getProblemList().add(temp);
            notifyObs();
            return true;
        }
        return false;
    }

    private void calculateStartDateAndEndDate(Problem problem) {
        if ((problem.getStartDay()==null && problem.getEndDay()==null) || problem.getNumberOfDuration()==null
                || problem.getNumberOfDuration()==0) return;
        long day=problem.getNumberOfDuration()*
                switch (problem.getCategoryOfDuration())
                        {
                            case "Дней"->1;
                            case "Месяцев"->30;
                            case "Лет"->365;
                            default -> 0;
                        };
        Calendar instance=Calendar.getInstance();

        if (problem.getStartDay()==null)
        {
            instance.setTime(problem.getEndDay());
            instance.add(Calendar.DATE, Math.toIntExact(-day));
            if (day!=0) problem.setStartDay(instance.getTime());
            else problem.setStartDay(problem.getEndDay());
        }
        else
        {
            instance.setTime(problem.getStartDay());
            instance.add(Calendar.DATE, Math.toIntExact(day));
            if (day!=0) problem.setEndDay(instance.getTime());
            else problem.setEndDay(problem.getStartDay());
        }
    }

    public void updateProblemNumOfDuration(Problem problem, Long duration) {
        Problem temp=problem.clone();
        temp.setNumberOfDuration(duration);
        updateProblem(temp);
    }

    public void updateTitle(ProjectEntity pe,String title){
        String temp=pe.getTitle();
        pe.setTitle(title);
        if ( ( pe instanceof Problem && !DAO.getProblemDAO().updateProblem((Problem) pe) ) ||
                ( pe instanceof Project && !DAO.getProjectDAO().updateProject((Project) pe) ) )
        {
            pe.setTitle(temp);
        }

        notifyObs();
    }

    private void updateProblem(Problem temp) {
        calculateStartDateAndEndDate(temp);
        for (int i = 0; i < projects.size(); i++) {
            for (int j = 0; j < projects.get(i).getProblemList().size(); j++) {
                if ( projects.get(i).getProblemList().get(j).getID()==temp.getID() && DAO.getProblemDAO().updateProblem(temp))
                {
                    projects.get(i).getProblemList().get(j).setTitle(temp.getTitle());
                    projects.get(i).getProblemList().get(j).setEndDay(temp.getEndDay());
                    projects.get(i).getProblemList().get(j).setStartDay(temp.getStartDay());
                    projects.get(i).getProblemList().get(j).setCategoryOfDuration(temp.getCategoryOfDuration());
                    projects.get(i).getProblemList().get(j).setNumberOfDuration(temp.getNumberOfDuration());
                    notifyObs();
                    return;
                }
            }
        }
    }

    public void updateProblemCategoryOfDuration(Problem problem,String category){
        Problem temp=problem.clone();
        temp.setCategoryOfDuration(category);
        updateProblem(temp);
    }
    private void calculateDuration(Problem problem) {
        if (problem.getStartDay() != null && problem.getEndDay() != null)
        {
            problem.setCategoryOfDuration("Дней");
            long difference = problem.getStartDay().getTime() - problem.getEndDay().getTime();
            difference /= (24 * 60 * 60 * 1000);
            problem.setNumberOfDuration(Math.abs(difference));
        }
    }
    public void updateProblemStartDate(Problem problem, Date date) {
        Problem temp=problem.clone();
        temp.setStartDay(date);
        if ( temp.getStartDay()!=null && temp.getEndDay()!=null && temp.getStartDay().compareTo( temp.getEndDay() )>=0 ){
            temp.setStartDay(temp.getEndDay());
        }
        calculateDuration(temp);
        updateProblem(temp);
    }
    public void updateProblemEndDate(Problem problem, Date date) {
        Problem temp=problem.clone();
        temp.setEndDay(date);
        if ( temp.getStartDay()!=null && temp.getEndDay().compareTo( temp.getStartDay() )<=0 ) {
            temp.setEndDay(temp.getStartDay());
        }
        calculateDuration(temp);
        updateProblem(temp);
    }
    public void removeProblemOfProject(Project project, Problem problem){
        if (DAO.getProblemDAO().deleteProblem(problem.getID()))
        {
            project.getProblemList().remove(problem);
            notifyObs();
        }
    }
    public void removeProject(Project project) {
        if (DAO.getProjectDAO().deleteProject(project.getID()))
        {
            for (int i = 0; i < project.getProblemList().size(); i++) {
                project.getProblemList().remove(project.getProblemList().get(i));
            }
            projects.remove(project);
            notifyObs();
        }
    }
    @Override
    public void AddObs(IObserver observer) {
        observers.add(observer);
    }
    @Override
    public void RemoveObs(IObserver observer) {
        observers.remove(observer);
    }
    @Override
    public void notifyObs() {
        for (IObserver observer : observers) {observer.handle(projects);}
    }
}