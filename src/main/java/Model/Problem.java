package Model;

import java.util.Date;

public class Problem extends ProjectEntity {
    private long ID;
    private long ProjectID;
    private String title;
    private Long numberOfDuration;
    private String categoryOfDuration;
    private Date StartDay;
    private Date EndDay; //optional

    @Override
    public String getStringStatus() {
        if (this.status==null) return "";
        return this.status.getName();
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    private Status status;//optional

    public Problem(long ID,long projectID,String title)
    {
        this.ID=ID;
        this.ProjectID=projectID;
        this.title = title;
        this.categoryOfDuration ="Дней";
        numberOfDuration=null;
        StartDay=null;
        EndDay=null;
        status=null;
    }

    public long getID() {
        return ID;
    }
    @Override
    public String getTitle(){
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public Long getNumberOfDuration() {
        return numberOfDuration;
    }
    public void setNumberOfDuration(Long numberOfDuration) {
        this.numberOfDuration = numberOfDuration;
    }
    @Override
    public Date getStartDay() {
        return StartDay;
    }
    public void setStartDay(Date startDay) {
        StartDay = startDay;
    }
    @Override
    public Date getEndDay() {
        return EndDay;
    }
    public void setEndDay(Date endDay) {
        EndDay = endDay;
    }
    @Override
    public String getCategoryOfDuration() {
        return categoryOfDuration;
    }

    public Problem clone()
    {
        Problem clone =new Problem(this.ID,this.ProjectID,this.title);
        clone.setCategoryOfDuration(this.categoryOfDuration);
        clone.setNumberOfDuration(this.numberOfDuration);
        clone.setStartDay(this.StartDay);
        clone.setEndDay(this.EndDay);
        clone.setStatus(this.status);

        return clone;
    }
    public long getProjectID() {
        return ProjectID;
    }
    public void setCategoryOfDuration(String categoryOfDuration) {
        this.categoryOfDuration = categoryOfDuration;
    }
}
