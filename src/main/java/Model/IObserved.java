package Model;

import com.example.GanttchDB.IObserver;

public interface IObserved{
    void AddObs(IObserver observer);
    void RemoveObs(IObserver observer);
    void notifyObs();
}

