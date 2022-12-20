package Model;

public class User {
    private long ID;
    private String login;
    public User(long id,String login)
    {
        this.ID=id;
        this.login=login;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }
}
