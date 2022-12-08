package DAO;

import Model.User;

//Interface for implementing the DAO User model
public interface IUserDAO {
    int getUser(String login,String password);
    int addUser(User newUser);
}