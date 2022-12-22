package DAO.H2;

import DAO.*;
import Model.User;

import java.sql.*;

//Concrete DAO class for h2 DB
public class H2DAOFactory extends DAOFactory {
    public static final String DB_URL = "jdbc:h2:/db/GanttchDB";
    public static final String DB_Driver = "org.h2.Driver";
    public static final String TableTask ="TASKS";
    public static final String TableProject ="PROJECTS";
    public static final String TableUsers ="USERS";
    public static final String TableStatuses ="STATUSES";

    H2UserDAO UserDAO;
    H2ProjectDAO projectDAO;
    H2ProblemDAO problemDAO;
    H2StatusDAO statusDAO;

    public H2DAOFactory()
    {
        UserDAO=new H2UserDAO(TableUsers,DB_URL);
        projectDAO=new H2ProjectDAO(TableProject,DB_URL);
        problemDAO=new H2ProblemDAO(TableTask,DB_URL);
        statusDAO=new H2StatusDAO(TableStatuses,DB_URL);
        try {
            Class.forName(DB_Driver);
            Connection connection = DriverManager.getConnection(DB_URL);
            DatabaseMetaData md = connection.getMetaData();
            ResultSet rs = md.getTables(null, null, TableTask, null);
            if (!rs.next()) {
                createTableUsers();
                createTableStatuses();
                createTableProjects();
                createTableTasks();
            }
            connection.close();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            System.out.println("JDBC драйвер для СУБД не найден!");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ошибка SQL!");
        }
    }

    /**
     *  Method for creating a table with statuses in the database
     */
    private void createTableStatuses()
    {
        String createTableSQL = "CREATE TABLE  "+TableStatuses+" (\n" +
                "ID INT NOT NULL auto_increment, \n" +
                "UserID INT NOT NULL,\n" +
                "title varchar(30) NOT NULL, \n" +
                "Color INT NOT NULL, \n" +
                "PRIMARY KEY (ID),\n" +
                "FOREIGN KEY (UserID) references USERS(ID)\n" +
                ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    /**
     * Method for creating a table with users in the database
     */
    private void createTableUsers()
    {
        String createTableSQL = "CREATE TABLE "+ TableUsers +" (\n" +
                "ID INT NOT NULL auto_increment, \n" +
                "login varchar(12) NOT NULL UNIQUE,\n" +
                "password varchar(12) NOT NULL,\n" +
                "PRIMARY KEY (ID) \n" +
                ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }

        UserDAO.addUser(new User(1,"test","test"));
    }

    //Method for creating a table with projects in the database
    private void createTableProjects()
    {
        String createTableSQL = "CREATE TABLE  "+TableProject+" (\n" +
                "ID INT NOT NULL auto_increment, \n" +
                "UserID INT NOT NULL,\n" +
                "title varchar(20) NOT NULL,\n" +
                "PRIMARY KEY (ID),\n" +
                "FOREIGN KEY (UserID) references USERS(ID) \n" +
                ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }

    //Method for creating a table with tasks in the database
    private void createTableTasks()
    {
        String createTableSQL = "CREATE TABLE "+ TableTask +" (\n" +
                "ID INT NOT NULL auto_increment, \n" +
                "IDProject INT NOT NULL, \n" +
                "Title varchar(20) NOT NULL, \n" +
                "StartDate DateTime, \n" +
                "EndDate DateTime, \n" +
                "IDStatus INT, \n" +
                "PRIMARY KEY (ID,IDProject),\n" +
                "FOREIGN KEY (IDProject) references Projects(ID), \n" +
                "FOREIGN KEY (IDStatus) references STATUSES(ID) ON DELETE SET NULL \n" +
                ")";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.execute(createTableSQL);
            }
        } catch (SQLException ignored) {
        }
    }
    @Override
    public IUserDAO getUserDAO() {
        return UserDAO;
    }

    @Override
    public IProblemDAO getProblemDAO() {
        return problemDAO;
    }

    @Override
    public IProjectDAO getProjectDAO() {
        return projectDAO;
    }

    @Override
    public IStatusDAO getStatusDAO() {
        return statusDAO;
    }
}
