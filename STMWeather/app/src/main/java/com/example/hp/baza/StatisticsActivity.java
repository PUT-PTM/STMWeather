package com.example.hp.baza;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;
import java.util.ListIterator;


public class StatisticsActivity extends AppCompatActivity
{

    private String[] iNumber = new String[] {
            "1", "2", "3", "4", "5",
            "6", "7", "8" , "9", "10"
    };

    private RadioGroup radioMeasurementGroup;
    private RadioButton radioMeasurementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        radioMeasurementGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioMeasurementButton=(RadioButton)findViewById(R.id.radioButton);
        drawChart(radioMeasurementButton);
    }

    ZarzadcaBazy toChart = new ZarzadcaBazy(this);
    //List<Measurement> measurements = toChart.giveAll();
    //listIterator(measurements);
    //int[] x_values = { 1,2,3,4,5,6,7,8,9,10 };
    //int[] y_values = { 0,0,0,0,0,0,0,0,0,0  };
      int[] x_values;
      int[] y_values;

    private void drawChart(RadioButton rb)
    {

        ZarzadcaBazy toChart = new ZarzadcaBazy(this);
        List<Measurement> measurements = toChart.giveAll();
        this.listIterator(measurements,rb);
        //this.listIterator(measurements,rb);
     /*   int[] x_values = { 1,2,3,4,5,6,7,8,9,10 };
        int[] y_values = new int[10];
    */
        // Creating an  XYSeries for Expense
        XYSeries expenseSeries = new XYSeries("Expense");

        // Adding data to Expense Series
        for(int i=0;i<x_values.length;i++){
            expenseSeries.add(x_values[i], y_values[i]);

            if(i>9)
            {
                break;
            }
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset xyMultipleSeriesDataset = new XYMultipleSeriesDataset();
        // Adding Expense Series to the dataset
        xyMultipleSeriesDataset.addSeries(expenseSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer renderer = new XYSeriesRenderer();
        renderer.setColor(Color.BLUE);
        renderer.setPointStyle(PointStyle.CIRCLE);
        renderer.setFillPoints(true);
        renderer.setLineWidth(3);
        renderer.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Temperature Chart");
        multiRenderer.setXTitle("Previous 10 measurements");
        multiRenderer.setYTitle("Temperature");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setShowGrid(true);
        for(int i=0;i<x_values.length;i++)
        {
            multiRenderer.addXTextLabel(i+1, iNumber[i]);
        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(renderer);

        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);

        // Creating a Line Chart
        View chart = ChartFactory.getLineChartView(getBaseContext(), xyMultipleSeriesDataset, multiRenderer);

        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(chart,0);
    }
/*
    private static void listIterator(List<Measurement> list) {
        System.out.println("#3. List iterator");
        for (ListIterator<Measurement> it = list.listIterator(); it.hasNext();) {
            System.out.println(it.nextIndex() + " " + it.next());
        }
    }
*/
/*    private void listIterator(List<Measurement> list)
    {
         int i = 0;
         int amount_of_elements = list.size();
         x_values = new int[amount_of_elements];
         y_values = new int[amount_of_elements];
         for (ListIterator<Measurement> it = list.listIterator(); it.hasNext();)
         {
            // if(i>=9)
            //     break;
            // else
             //{
                 //System.out.println(it.nextIndex() + " " + it.next());
                 String tempTemp = it.next().getTemperature();
                 int tempTemp2 = Integer.parseInt(tempTemp);
                 x_values[i] = i + 1;
                 y_values[i] = tempTemp2;
                 i++;
            // }
         }
    }
*/

    private void listIterator(List<Measurement> list, RadioButton rb)
    {
        int i = 0;
        int x = R.id.radioButton3;
        int amount_of_elements = list.size();
        x_values = new int[amount_of_elements];
        y_values = new int[amount_of_elements];

        if(x==R.id.radioButton)
        {
            for (ListIterator<Measurement> it = list.listIterator(); it.hasNext(); )
            {

                     System.out.println(it.nextIndex() + " " + it.next());
                     String tempTemp = it.next().getTemperature();
                     int tempTemp2 = Integer.parseInt(tempTemp);
                     x_values[i] = i + 1;
                     y_values[i] = tempTemp2;
                     i++;

            }
        }

        else if(x==R.id.radioButton2)
        {
            for (ListIterator<Measurement> it = list.listIterator(); it.hasNext(); )
            {

                System.out.println(it.nextIndex() + " " + it.next());
                String tempTemp = it.next().getHumidity();
                int tempTemp2 = Integer.parseInt(tempTemp);
                x_values[i] = i + 1;
                y_values[i] = tempTemp2;
                i++;

            }
        }

        else if(x==R.id.radioButton3)
        {
            for (ListIterator<Measurement> it = list.listIterator(); it.hasNext(); )
            {

                System.out.println(it.nextIndex() + " " + it.next());
                String tempTemp = it.next().getPressure();
                int tempTemp2 = Integer.parseInt(tempTemp);
                x_values[i] = i + 1;
                y_values[i] = tempTemp2;
                i++;

            }
        }

    }

    public void returnToMainActivity(View view)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    public void refresh(View view)
    {
        int selectedId=radioMeasurementGroup.getCheckedRadioButtonId();
        radioMeasurementButton=(RadioButton)findViewById(selectedId);
        drawChart(radioMeasurementButton);
        //listIterator(list, radioMeasurementButton);
        //Toast.makeText(StatisticsActivity.this,radioMeasurementButton.getText(), Toast.LENGTH_SHORT).show();
    }

}
