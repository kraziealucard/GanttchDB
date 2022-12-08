package DAO.H2;

import DAO.IProjectDAO;
import Model.Project;

import java.sql.*;
import java.util.ArrayList;


public class H2ProjectDAO implements IProjectDAO {

    String TableProject;
    String DB_URL;

    public H2ProjectDAO(String tableProject, String db_url)
    {
        TableProject=tableProject;
        DB_URL=db_url;
    }

    /**
     * Method for getting projects of users
     * @param projects list for recording projects
     * @param IDUser of the projects we want to receive
     */
    @Override
    public void getProjectOfUser(ArrayList<Project> projects, long IDUser) {
        projects.clear();
        String selection = "select * from "+ TableProject + " where UserID='" + IDUser +"' order by ID";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Project element;
                    element=new Project(rs.getLong("ID"),rs.getString("TITLE"),IDUser);
                    projects.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Method for update project
     * @param project for update
     * @return is it successful?
     */
    @Override
    public boolean updateProject(Project project) {
        String updateTableSQL = String.format("UPDATE "+ TableProject
                        + " SET TITLE='%s' where ID=%s;",
                project.getTitle(),project.getID());
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
     * Method for add project into DB
     * @param project for add DB
     * @return ID project from DB
     */
    @Override
    public int addProject(Project project) {
        String insertTableSQL = "INSERT INTO "+ TableProject
                + " (TITLE, USERID) " + "VALUES "
                + "('"+project.getTitle()+"',"+project.getUserID()+")";
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
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableProject+" ORDER BY ID DESC");
                rs.next();

                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Method for delete project and tasks of project from DB
     * @param IDProject of project
     * @return is it successful?
     */
    @Override
    public boolean deleteProject(long IDProject) {
        String deleteTableSQL=String.format("DELETE FROM TASKS WHERE IDPROJECT = "+IDProject);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
            }
            deleteTableSQL = String.format("DELETE from "+ TableProject +" WHERE ID=%s;",IDProject);
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
