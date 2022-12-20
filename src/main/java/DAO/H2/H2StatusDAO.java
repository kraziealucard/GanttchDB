package DAO.H2;

import DAO.IStatusDAO;
import Model.Status;

import java.sql.*;
import java.util.ArrayList;

public class H2StatusDAO implements IStatusDAO {
    String TableStatus;
    String DB_URL;

    public H2StatusDAO(String tableStatus, String db_url)
    {
        TableStatus = tableStatus;
        DB_URL=db_url;
    }

    @Override
    public void getStatusOfUser(ArrayList<Status> statuses, long IDUser) {
        statuses.clear();
        String selection = "select * from "+ TableStatus + " where UserID='" + IDUser +"' order by ID";
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                ResultSet rs = statement.executeQuery(selection);
                while (rs.next()) {
                    Status element;
                    element=new Status(rs.getLong("ID"),IDUser,rs.getInt("COLOR"));
                    element.setName(rs.getString("TITLE"));
                    statuses.add(element);
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public boolean updateStatus(Status status) {
        String updateTableSQL = String.format("UPDATE "+ TableStatus
                        + " SET TITLE='%s', COLOR='%s' where ID=%s;",
                status.getName(),status.getColor().getRGB(), status.getID());
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

    @Override
    public int addStatus(Status status) {
        String insertTableSQL = "INSERT INTO "+ TableStatus
                + " (TITLE, COLOR, USERID) " + "VALUES "
                + "('"+status.getName()+"',"+status.getColor().getRGB()+","+status.getUserId()+")";
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
                ResultSet rs=statement.executeQuery("SELECT TOP 1 ID FROM "+ TableStatus +" ORDER BY ID DESC");
                rs.next();

                return rs.getInt("ID");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    @Override
    public boolean deleteStatus(long IDStatus) {
        String deleteTableSQL=String.format("DELETE FROM TASKS WHERE IDPROJECT = "+IDStatus);
        try (Connection dbConnection = DriverManager.getConnection(DB_URL)) {
            assert dbConnection != null;
            try (Statement statement = dbConnection.createStatement()) {
                statement.executeUpdate(deleteTableSQL);
            }
            deleteTableSQL = String.format("DELETE from "+ TableStatus +" WHERE ID=%s;",IDStatus);
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
