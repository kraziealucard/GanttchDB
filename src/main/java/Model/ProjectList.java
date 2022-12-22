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
    ArrayList<Status> statuses;

    long UserID;

    public ProjectList(long userID, DAOFactory dao, ArrayList<Status> statuses) {
        this.statuses = statuses;
        this.DAO = dao;
        this.UserID = userID;
        projects = new ArrayList<>();
        observers = new ArrayList<>();
        loadData();
    }

    /**
     * Method for load data from DataBase
     */
    private void loadData() {
        DAO.getProjectDAO().getProjectOfUser(projects, UserID);
        for (int i = 0; i < projects.size(); i++) {
            DAO.getProblemDAO().getProblemsOfProject(projects.get(i), statuses);
            for (int j = 0; j < projects.get(i).getProblemList().size(); j++) {
                calculateDuration(projects.get(i).getProblemList().get(j));
            }
        }
    }

    /**
     * Method for add Project
     *
     * @param projectName of project
     * @return Success status of save to database
     */
    public boolean addProject(String projectName) {
        Project temp = new Project(-1, projectName, UserID);
        long id = DAO.getProjectDAO().addProject(temp);
        if (id != -1) {
            temp = new Project(id, projectName, UserID);
            projects.add(temp);
            notifyObs();
            return true;
        }
        return false;
    }

    /**
     * Method for add Problem
     * @param project in which we add the task
     * @param nameOfProblem for adds
     * @return Success status of save to database
     */
    public boolean addProblemToProject(Project project, String nameOfProblem) {
        Problem temp = new Problem(-1, project.getID(), nameOfProblem);
        long id = DAO.getProblemDAO().addProblem(temp);
        if (id != -1) {
            temp = new Problem(id, project.getID(), nameOfProblem);
            project.getProblemList().add(temp);
            notifyObs();
            return true;
        }
        return false;
    }

    /**
     * Method for recalculating the date of the beginning and end of the task
     * @param problem in which we want to recalculate the dates
     */
    private void calculateStartDateAndEndDate(Problem problem) {
        if ((problem.getStartDay() == null && problem.getEndDay() == null) || problem.getNumberOfDuration() == null
                || problem.getNumberOfDuration() == 0) return;
        long day = problem.getNumberOfDuration() *
                switch (problem.getCategoryOfDuration()) {
                    case "Дней" -> 1;
                    case "Месяцев" -> 30;
                    case "Лет" -> 365;
                    default -> 0;
                };
        Calendar instance = Calendar.getInstance();

        if (problem.getStartDay() == null) {
            instance.setTime(problem.getEndDay());
            instance.add(Calendar.DATE, Math.toIntExact(-day));
            if (day != 0) problem.setStartDay(instance.getTime());
            else problem.setStartDay(problem.getEndDay());
        } else {
            instance.setTime(problem.getStartDay());
            instance.add(Calendar.DATE, Math.toIntExact(day));
            if (day != 0) problem.setEndDay(instance.getTime());
            else problem.setEndDay(problem.getStartDay());
        }
    }

    /**
     * Set a new duration value for a task
     * @param problem in which we set a new duration value
     * @param duration new value
     */
    public void updateProblemNumOfDuration(Problem problem, Long duration) {
        Problem temp = problem.clone();
        temp.setNumberOfDuration(duration);
        updateProblem(temp);
    }

    /**
     * Method for setting new value for tittle for the project and problem
     * @param pe Project or Problem in which we set a new value
     * @param title new value
     */
    public void updateTitle(ProjectEntity pe, String title) {
        String temp = pe.getTitle();
        pe.setTitle(title);
        if ((pe instanceof Problem && !DAO.getProblemDAO().updateProblem((Problem) pe)) ||
                (pe instanceof Project && !DAO.getProjectDAO().updateProject((Project) pe))) {
            pe.setTitle(temp);
        }

        notifyObs();
    }

    /**
     * Method for updating a task change in the database
     * @param temp a task for update
     */
    private void updateProblem(Problem temp) {
        calculateStartDateAndEndDate(temp);
        for (int i = 0; i < projects.size(); i++) {
            for (int j = 0; j < projects.get(i).getProblemList().size(); j++) {
                if (projects.get(i).getProblemList().get(j).getID() == temp.getID() && DAO.getProblemDAO().updateProblem(temp)) {
                    projects.get(i).getProblemList().get(j).setTitle(temp.getTitle());
                    projects.get(i).getProblemList().get(j).setEndDay(temp.getEndDay());
                    projects.get(i).getProblemList().get(j).setStartDay(temp.getStartDay());
                    projects.get(i).getProblemList().get(j).setStatus(temp.getStatus());
                    projects.get(i).getProblemList().get(j).setCategoryOfDuration(temp.getCategoryOfDuration());
                    projects.get(i).getProblemList().get(j).setNumberOfDuration(temp.getNumberOfDuration());
                    notifyObs();
                    return;
                }
            }
        }
    }

    /**
     * Method for update Category Of Duration for tusk
     * @param problem a task for update
     * @param category new value Category Of Duration
     */
    public void updateProblemCategoryOfDuration(Problem problem, String category) {
        Problem temp = problem.clone();
        temp.setCategoryOfDuration(category);
        updateProblem(temp);
    }

    /**
     * Method in which the duration of the work is recalculated
     * @param problem in which we calculate the duration of the work
     */
    private void calculateDuration(Problem problem) {
        if (problem.getStartDay() != null && problem.getEndDay() != null) {
            problem.setCategoryOfDuration("Дней");
            long difference = problem.getStartDay().getTime() - problem.getEndDay().getTime();
            difference /= (24 * 60 * 60 * 1000);
            problem.setNumberOfDuration(Math.abs(difference));
        }
    }

    /**
     * Method in which we set a new value for the start date of the task
     * @param problem in which we set a new value for the execution start date
     * @param date new Value
     */
    public void updateProblemStartDate(Problem problem, Date date) {
        Problem temp = problem.clone();
        temp.setStartDay(date);
        if (temp.getStartDay() != null && temp.getEndDay() != null && temp.getStartDay().compareTo(temp.getEndDay()) >= 0) {
            temp.setStartDay(temp.getEndDay());
        }
        calculateDuration(temp);
        updateProblem(temp);
    }

    /**
     * Method in which we set a new value for the end date of the task
     * @param problem in which we set a new value for the execution end date
     * @param date new Value
     */
    public void updateProblemEndDate(Problem problem, Date date) {
        Problem temp = problem.clone();
        temp.setEndDay(date);
        if (temp.getStartDay() != null && temp.getEndDay().compareTo(temp.getStartDay()) <= 0) {
            temp.setEndDay(temp.getStartDay());
        }
        calculateDuration(temp);
        updateProblem(temp);
    }

    /**
     * Method in which we set a new value for the status of the task
     * @param problem in which we set a new value for the execution end date
     * @param status new Value
     */
    public void updateProblemStatus(Problem problem, String status) {
        Problem temp = problem.clone();
        Status stat = null;
        if (!status.isBlank()) {
            for (int i = 0; i < statuses.size(); i++) {
                if (status.equals(statuses.get(i).getName())) stat = statuses.get(i);
            }
        }
        temp.setStatus(stat);
        updateProblem(temp);
    }

    /**
     * Method for remove task from DataBase
     * @param project from which the task will be removed
     * @param problem to be deleted
     */
    public void removeProblemOfProject(Project project, Problem problem) {
        if (DAO.getProblemDAO().deleteProblem(problem.getID())) {
            project.getProblemList().remove(problem);
            notifyObs();
        }
    }

    /**
     * Method for remove project and his tasks from DataBase
     * @param project to be deleted
     */
    public void removeProject(Project project) {
        if (DAO.getProjectDAO().deleteProject(project.getID())) {
            for (int i = 0; i < project.getProblemList().size(); i++) {
                project.getProblemList().remove(project.getProblemList().get(i));
            }
            projects.remove(project);
            notifyObs();
        }
    }

    /**
     * Method to add an observer to the list of observers
     * @param observer to be added to the list of observers
     */
    @Override
    public void AddObs(IObserver observer) {
        observers.add(observer);
    }

    /**
     * Method that removes an observer from the list of observers
     * @param observer to be removes to the list of observers
     */
    @Override
    public void RemoveObs(IObserver observer) {
        observers.remove(observer);
    }

    /**
     * Method notifying observers
     */
    @Override
    public void notifyObs() {
        for (IObserver observer : observers) {
            observer.handle(projects);
        }
    }


    public ArrayList<Project> getProjects() {
        return projects;
    }
}