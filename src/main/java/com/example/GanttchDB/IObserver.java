package com.example.GanttchDB;

import Model.Project;

import java.util.ArrayList;

public interface IObserver {
    /**
     *Method for responding to data changes
     * @param Data observed
     */
    void handle(ArrayList<Project> Data);
}
