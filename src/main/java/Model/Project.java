package Model;

import java.util.ArrayList;
import java.util.List;

public class Project extends ProjectEntity {
    private final long ID;
    private String title;
    private final long userID;
    private ArrayList<Problem> problemList;

    public  Project(long ID,String title,long UserID)
    {
        this.ID=ID;
        this.title = title;
        problemList = new ArrayList<>();
        userID = UserID;
    }

    public long getID() {
        return ID;
    }


    @Override
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Problem> getProblemList() {
        return problemList;
    }

    public void setProblemList(ArrayList<Problem> problemList) {
        this.problemList = problemList;
    }

    public long getUserID() {
        return userID;
    }
}
