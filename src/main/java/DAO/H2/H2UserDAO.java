package DAO.H2;

import DAO.IUserDAO;
import Model.User;

import java.sql.*;

class H2UserDAO implements IUserDAO {
    private String TableUsers;
    private String DB_URL;

    H2UserDAO(String tableUsers, String db_URL)
    {
        this.DB_URL=db_URL;
        this.TableUsers=tableUsers;
    }

    /**
     * Method for getting user from DB
     * @param login of the user we want to receive
     * @param password of the user we want to receive
     * @return ID user
     */
    @Override
    public int getUser(String login, String password) {
        String selection = "SELECT * FROM "+ TableUsers+" WHERE LOGIN = '"+login+"' AND PASSWORD='"+password+"'";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                if (rs.next()) {
                    return rs.getInt("ID");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    /**
     * Method for add User into DB
     * @param newUser for add DB
     * @return ID user from DB
     */
    @Override
    public int addUser(User newUser) {
        String insertTableSQL = "INSERT INTO "+ TableUsers
                + " (Login,Password) " + "VALUES "
                + "('"+newUser.getLogin()+"','"+newUser.getPassword()+"')";
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
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+TableUsers+" ORDER BY ID DESC");
                rs.next();
                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
