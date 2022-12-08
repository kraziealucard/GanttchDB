package Model;

import com.example.GanttchDB.IObserver;

public interface IObserved{
    public void AddObs(IObserver observer);
    public void RemoveObs(IObserver observer);
    public void notifyObs();
}

