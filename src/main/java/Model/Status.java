package Model;

import java.awt.*;

public class Status {
    private long Id;
    private long UserId;
    private String Name;
    private Color color;

    public Status(long ID,long userID,int color){
        this.Id=ID;
        this.Name=null;
        this.UserId = userID;
        this.color=new Color(color);
    }

    public Status clone() {
        Status clone=new Status(this.Id,this.UserId,this.color.getRGB());
        clone.setName(this.Name);
        return clone;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public long getID() {
        return Id;
    }

    public void setID(int id) {
        Id = id;
    }

    public long getUserId() {
        return UserId;
    }
}
