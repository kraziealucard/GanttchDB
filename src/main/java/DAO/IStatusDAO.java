package DAO;

import Model.Status;

import java.util.ArrayList;

public interface IStatusDAO {
    void getStatusOfUser(ArrayList<Status> projects, long IDUser);
    boolean updateStatus(Status status);
    int addStatus(Status status);
    boolean deleteStatus(long IDProject);

}
