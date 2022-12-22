package Model;

public class User {
    private long ID;
    private String login;
    private String password;
    public User(long id,String login,String password)
    {
        this.ID=id;
        this.login=login;
        this.password=password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
