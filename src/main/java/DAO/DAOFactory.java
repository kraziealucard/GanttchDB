package DAO;

import DAO.H2.H2DAOFactory;

// Abstract class DAO Factory
public abstract class DAOFactory {

    // List of DAO types supported by the factory
    public static final int H2 = 1;
    public abstract IUserDAO getUserDAO();
    public abstract IProblemDAO getProblemDAO();
    public abstract IProjectDAO getProjectDAO();

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch (whichFactory) {
            case H2: return new H2DAOFactory();
            default: return new H2DAOFactory();
        }
    }

}