package DAO.H2;

import DAO.IProblemDAO;
import Model.Problem;
import Model.Project;

import java.sql.*;

public class H2ProblemDAO implements IProblemDAO {

    String TableTask;
    String DB_URL;
    H2ProblemDAO(String TableTask, String db_URL)
    {
        this.TableTask = TableTask;
        this.DB_URL=db_URL;
    }

    /**
     * Method for getting project tasks
     * @param project of the tasks we want to receive
     */
    @Override
    public void getProblemsOfProject(Project project) {
        project.getProblemList().clear();
        String selection = "select * from "+ TableTask + " WHERE IDPROJECT =" + project.getID() +" order by ID";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Problem element;
                    element=new Problem(rs.getLong("ID"),project.getID(),rs.getString("TITLE"));
                    element.setStartDay(rs.getDate("STARTDATE"));
                    element.setEndDay(rs.getDate("ENDDATE"));
                    project.getProblemList().add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method for updating the task in the database
     * @param problem for update
     * @return is it successful?
     */
    @Override
    public boolean updateProblem(Problem problem) {
        String updateTableSQL = "UPDATE "+ TableTask +
                " SET TITLE='"+problem.getTitle()+"', IDPROJECT ="+problem.getProjectID()+", STARTDATE = ";
        if (problem.getStartDay()!=null)
        {
            updateTableSQL+="'"+new Timestamp(problem.getStartDay().getTime())+"', ENDDATE = ";
        }
        else
        {
            updateTableSQL+="NULL, ENDDATE = ";
        }

        if (problem.getEndDay()!=null)
        {
            updateTableSQL+="'"+new Timestamp(problem.getEndDay().getTime())+"' WHERE ID = "+problem.getID();
        }
        else
        {
            updateTableSQL+="NULL WHERE ID = "+problem.getID();
        }

        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(updateTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Method for add problem into DB
     * @param problem for add DB
     * @return ID problem from DB
     */
    @Override
    public int addProblem(Problem problem) {
        String insertTableSQL = "INSERT INTO "+ TableTask
                + " (IDPROJECT, TITLE) " + "VALUES "
                + "("+problem.getProjectID()+",'"+problem.getTitle()+"')";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(insertTableSQL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return -1;
        }
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try(Statement statement = dbConnection.createStatement()) {
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+ TableTask +" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Method for delete task from DB
     * @param IDProblem of task
     * @return is it successful?
     */
    @Override
    public boolean deleteProblem(long IDProblem) {
        String deleteTableSQL = String.format("DELETE from "+ TableTask +" WHERE ID=%s;",IDProblem);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }
}
