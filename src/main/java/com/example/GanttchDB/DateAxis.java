package com.example.GanttchDB;

import javafx.geometry.Side;
import javafx.scene.chart.ValueAxis;
import javafx.scene.paint.Color;
import javafx.util.StringConverter;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

//DateAxis for GanttChart
public class DateAxis extends ValueAxis<Number>{

    // List of ticks
    private List<Number> ticks;
    private double different;
    private DateTickFormatter defaultFormatter;
    public DateAxis(Number... ticks) {
        super();

        this.ticks=new ArrayList<>();
        this.setAutoRanging(false);
        this.minHeight(0);
        this.minWidth(0);
        this.setTicks(ticks);
        this.setTickLabelFill(Color.CHOCOLATE);
        //this.defaultFormatter = new NumberAxis.DefaultFormatter(new NumberAxis());

        this.setSide(Side.TOP);
    }

    /**
     * Method for installation ticks on axis
     * @param ticks for installation on axis
     */
    public void setTicks(Number... ticks)
    {
        this.ticks.clear();
        this.tickMarksUpdated();
        this.ticks=new ArrayList<>(List.of(ticks));
        setRange();
        this.tickMarksUpdated();

        this.defaultFormatter = new DateTickFormatter();
        this.setTickLabelFormatter(defaultFormatter);
    }

    /**
     * Method for set the range on the axis
     */
    private void setRange()
    {
        if (ticks.size()==0) return;
        different=0;

        double lowerBound=ticks.get(0).doubleValue();
        double upperBound=ticks.get(0).doubleValue();

        for (int i = 0; i < ticks.size(); i++) {
            if (ticks.get(i).doubleValue()>upperBound) upperBound = ticks.get(i).doubleValue();
            if (ticks.get(i).doubleValue()<lowerBound) lowerBound = ticks.get(i).doubleValue();
        }
        different=Math.abs(upperBound-lowerBound);
        different/=this.ticks.size();

        setLowerBound(lowerBound-different);
        setUpperBound(upperBound+different);

        currentLowerBound.set(lowerBound);
        setScale(1);
    }

    @Override
    protected List<Number> calculateMinorTickMarks() {
        return new ArrayList<>();
    }

    @Override
    protected void setRange(Object range, boolean animate) {}

    @Override
    protected Object getRange() {
        return new double[]{
                getLowerBound(),
                getUpperBound(),
                getScale(),
        };
    }


    @Override
    protected List<Number> calculateTickValues(double length, Object range) {
        return ticks;
    }

    @Override
    protected String getTickMarkLabel(Number value) {
        StringConverter<Number> formatter = getTickLabelFormatter();
        if (formatter == null) formatter = defaultFormatter;
        return formatter.toString(value);
    }
    private class DateTickFormatter extends StringConverter<Number>
    {
        private static final SimpleDateFormat F = new SimpleDateFormat( "dd.MM" );
        private static final SimpleDateFormat Y = new SimpleDateFormat("dd.MM.yyyy");
        @Override
        public String toString( Number date )
        {
            long x=31536000000L;
            if (different>= x) return Y.format( new Date( date.longValue() ) );
            return F.format( new Date( date.longValue() ) );
        }

        @Override
        public Number fromString( String paramString )
        {
            try
            {
                long x=31536000000L;
                if (different>=x) return Y.parse(paramString).getTime();
                return F.parse(paramString).getTime();
            }
            catch( ParseException pe )
            {
                throw new RuntimeException( "Failed to parse date", pe );
            }
        }
    }
}
