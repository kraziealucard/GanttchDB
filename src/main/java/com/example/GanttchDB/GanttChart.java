package com.example.GanttchDB;
import Model.Project;
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.SplitPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

import java.util.ArrayList;
import java.util.List;

//A class that draws a gantt chart based on data
public class GanttChart implements IObserver {
    private DateAxis NA;
    private CategoryAxis CA;
    private StackedBarChart<Number, String> Chart;
    private final XYChart.Series<Number,String> SeriesStep=new XYChart.Series<>();
    private final XYChart.Series<Number,String> SeriesNotStep=new XYChart.Series<>();
    private final XYChart.Series<Number,String> SeriesNotStepProject=new XYChart.Series<>();

    GanttChart(SplitPane SP)
    {
        this.NA=new DateAxis();
        NumberAxis ae=new NumberAxis();

        this.CA=new CategoryAxis();
        this.CA.minHeight(0);
        this.CA.setMinHeight(0);
        this.CA.setTickLabelFill(Color.CHOCOLATE);
        CA.setTickLabelFont(Font.font(16));

        this.Chart=new StackedBarChart<>(NA,CA);
        Chart.setAnimated(false);
        this.Chart.minHeight(0);
        this.Chart.minWidth(0);



        this.Chart.setMinSize(0,0);
        this.Chart.setLegendVisible(false);
        SP.getItems().add(Chart);
    }

    /**
     *Method for responding to data changes
     * @param Data observed
     */
    @Override
    public void handle(ArrayList<Project> Data) {
        Chart.getData().clear();
        SeriesStep.getData().clear();
        SeriesNotStep.getData().clear();
        SeriesNotStepProject.getData().clear();
        this.Chart.getData().add(SeriesStep);
        this.Chart.getData().add(SeriesNotStep);
        this.Chart.getData().add(SeriesNotStepProject);
        Number startDate,endDate,startP=null,endP=null;
        String name;
        List<Number> temp=new ArrayList<>();
        for (int i = Data.size()-1; i >= 0; i--) {
            startP=null;
            endP=null;
            if (Data.get(i).getProblemList().size()==0) continue;
            for (int j = Data.get(i).getProblemList().size()-1; j >=0 ; j--) {
                if (Data.get(i).getProblemList().get(j).getStartDay()==null || Data.get(i).getProblemList().get(j).getEndDay()==null ||
                        Data.get(i).getProblemList().get(j).getTitle()==null)
                {
                    continue;
                }
                startDate=Data.get(i).getProblemList().get(j).getStartDay().getTime();
                endDate=Data.get(i).getProblemList().get(j).getEndDay().getTime();

                if ( startP==null || startP.longValue()>startDate.longValue() )
                {
                    startP=startDate;
                }
                if ( endP==null || endP.longValue()<endDate.longValue() )
                {
                    endP=endDate;
                }

                temp.add(startDate.longValue());
                temp.add(endDate.longValue());
                endDate=endDate.longValue()-startDate.longValue();
                name=Data.get(i).getProblemList().get(j).getTitle();

                for (int k = Chart.getData().get(0).getData().size()-1; k >=0; k--) {
                    if (Chart.getData().get(0).getData().get(k).getYValue() == name )
                    {
                        name=" "+name;
                        break;
                    }
                }

                SeriesStep.getData().add(new XYChart.Data<>(startDate,name));
                SeriesNotStep.getData().add(new XYChart.Data<>(endDate,name));

            }
            if (startP != null)
            {
                String nameP="Проект \""+Data.get(i).getTitle()+"\"";
                endP=endP.longValue()-startP.longValue();
                SeriesStep.getData().add(new XYChart.Data<>(startP,nameP));
                SeriesNotStepProject.getData().add(new XYChart.Data<>(endP,nameP));
            }
        }
        NA.setTicks(temp.toArray(new Number[temp.size()]));
    }


    public Node getNode()
    {
        return Chart.getParent();
    }
}
