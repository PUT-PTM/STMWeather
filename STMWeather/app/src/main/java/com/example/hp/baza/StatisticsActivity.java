package com.example.hp.baza;

/*
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
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

    //private RadioGroup radioMeasurementGroup;
    //private RadioButton radioMeasurementButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        drawChart();
    }

    ZarzadcaBazy toChart = new ZarzadcaBazy(this);
    //List<Measurement> measurements = toChart.giveAll();
    //listIterator(measurements);
    //int[] x_values = { 1,2,3,4,5,6,7,8,9,10 };
    //int[] y_values = { 0,0,0,0,0,0,0,0,0,0  };
    int[] x_values;
    int[] y1_values;
    int[] y2_values;
    int[] y3_values;
    int[] y4_values;

    private void drawChart(){

        ZarzadcaBazy toChart = new ZarzadcaBazy(this);
        List<Measurement> measurements = toChart.giveAll();
        this.listIterator(measurements);
     /*   int[] x_values = { 1,2,3,4,5,6,7,8,9,10 };
        int[] y_values = new int[10];
    */
        // Creating an  XYSeries for Expense
  /*      XYSeries temperatureSeries = new XYSeries("Temperature");
        XYSeries humiditySeries = new XYSeries("Humidity");
        XYSeries sunSeries = new XYSeries("Sun");
        XYSeries rainSeries = new XYSeries("Rain");

        // Adding data to Series
        for(int i=0;i<x_values.length;i++)
        {
            //temperatureSeries.add(x_values[i], y1_values[i]);
            //humiditySeries.add(x_values[i], y2_values[i]);
            //sunSeries.add(x_values[i], y3_values[i]);
            //rainSeries.add(x_values[i], y4_values[i]);
            temperatureSeries.add(i, y1_values[i]);
            //humiditySeries.add(x_values[i], y2_values[i]);
            //sunSeries.add(x_values[i], y3_values[i]);
            //rainSeries.add(i, y4_values[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Expense Series to the dataset
        dataset.addSeries(temperatureSeries);
        dataset.addSeries(humiditySeries);
        dataset.addSeries(sunSeries);
        dataset.addSeries(rainSeries);

        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer temperatureRenderer = new XYSeriesRenderer();
        temperatureRenderer.setColor(Color.RED);
        temperatureRenderer.setPointStyle(PointStyle.CIRCLE);
        temperatureRenderer.setFillPoints(true);
        temperatureRenderer.setLineWidth(3);
        temperatureRenderer.setDisplayChartValues(true);

        XYSeriesRenderer humidityRenderer = new XYSeriesRenderer();
        temperatureRenderer.setColor(Color.GREEN);
        temperatureRenderer.setPointStyle(PointStyle.CIRCLE);
        temperatureRenderer.setFillPoints(true);
        temperatureRenderer.setLineWidth(3);
        temperatureRenderer.setDisplayChartValues(true);

        XYSeriesRenderer sunRenderer = new XYSeriesRenderer();
        temperatureRenderer.setColor(Color.YELLOW);
        temperatureRenderer.setPointStyle(PointStyle.CIRCLE);
        temperatureRenderer.setFillPoints(true);
        temperatureRenderer.setLineWidth(3);
        temperatureRenderer.setDisplayChartValues(true);

        XYSeriesRenderer rainRenderer = new XYSeriesRenderer();
        temperatureRenderer.setColor(Color.BLUE);
        temperatureRenderer.setPointStyle(PointStyle.CIRCLE);
        temperatureRenderer.setFillPoints(true);
        temperatureRenderer.setLineWidth(3);
        temperatureRenderer.setDisplayChartValues(true);


        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Measurements Chart");
        multiRenderer.setXTitle("Previous 10 measurements");
        multiRenderer.setYTitle("");
        multiRenderer.setZoomButtonsVisible(true);
        for(int i=0;i<x_values.length;i++){
            multiRenderer.addXTextLabel(i+1, iNumber[i]);
        }

        // Adding expenseRenderer to multipleRenderer
        multiRenderer.addSeriesRenderer(temperatureRenderer);
        multiRenderer.addSeriesRenderer(humidityRenderer);
        multiRenderer.addSeriesRenderer(sunRenderer);
        multiRenderer.addSeriesRenderer(rainRenderer);

        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);

        // Creating a Line Chart
        View chart = ChartFactory.getLineChartView(getBaseContext(), dataset, multiRenderer);

        // Adding the Line Chart to the LinearLayout
        chartContainer.addView(chart);
    }
/*
    private static void listIterator(List<Measurement> list) {
        System.out.println("#3. List iterator");
        for (ListIterator<Measurement> it = list.listIterator(); it.hasNext();) {
            System.out.println(it.nextIndex() + " " + it.next());
        }
    }
*/
  /*  private void listIterator(List<Measurement> list)
    {
         int i = 0;
         int amount_of_elements = list.size();
         x_values = new int[amount_of_elements];
         y1_values = new int[amount_of_elements];
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
                 y1_values[i] = tempTemp2;
                 i++;
            // }
         }
    }


    public void returnToMainActivity(View view)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

}
*/

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.util.List;
import java.util.ListIterator;

public class StatisticsActivity extends Activity {

    private GraphicalView mChart;

    private String[] iNumber = new String[] {
            "1", "2" , "3", "4", "5",
            "6", "7" , "8", "9", "10"
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        openChart();
    }

    int[] x;
    int[] y1;
    int[] y2;
    int[] y3;
    int[] y4;

    int booleanStringToInteger(String booleanString)
    {
        if(booleanString.equals("true"))
        {
            return 1;
        }
        else
        {
            return 0;
        }
    }

    private void openChart(){
        /*x = { 1,2,3,4,5,6,7,8,9,10 };
        temperature = { 2000,2500,2700,3000,2800,3500,3700,3800, 3900, 4000};
        humidity = {2200, 2700, 2900, 2800, 2600, 3000, 3300, 3400, 2000, 3000 };*/

        ZarzadcaBazy toChart = new ZarzadcaBazy(this);
        List<Measurement> measurements = toChart.giveAll();
        this.listIterator(measurements);

        // Creating an  XYSeries for Income
        XYSeries temperatureSeries = new XYSeries("Temperature");
        // Creating an  XYSeries for Expense
        XYSeries humiditySeries = new XYSeries("Humidity");
        // Adding data to Income and Expense Series
        XYSeries sunSeries = new XYSeries("Sun");
        XYSeries rainSeries = new XYSeries("Rain");
        for(int i=0;i<x.length;i++){
            temperatureSeries.add(x[i], y1[i]);
            humiditySeries.add(x[i], y2[i]);
            sunSeries.add(x[i], y3[i]);
            rainSeries.add(x[i], y4[i]);
        }

        // Creating a dataset to hold each series
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        // Adding Income Series to the dataset
        dataset.addSeries(temperatureSeries);
        // Adding Expense Series to dataset
        dataset.addSeries(humiditySeries);
        dataset.addSeries(sunSeries);
        dataset.addSeries(rainSeries);


        // Creating XYSeriesRenderer to customize incomeSeries
        XYSeriesRenderer temperatureRenderer = new XYSeriesRenderer();
        temperatureRenderer.setColor(Color.RED);
        temperatureRenderer.setPointStyle(PointStyle.CIRCLE);
        temperatureRenderer.setFillPoints(true);
        temperatureRenderer.setLineWidth(2);
        temperatureRenderer.setDisplayChartValues(true);


        // Creating XYSeriesRenderer to customize expenseSeries
        XYSeriesRenderer humidityRenderer = new XYSeriesRenderer();
        humidityRenderer.setColor(Color.BLUE);
        humidityRenderer.setPointStyle(PointStyle.CIRCLE);
        humidityRenderer.setFillPoints(true);
        humidityRenderer.setLineWidth(1);
        humidityRenderer.setDisplayChartValues(true);


        XYSeriesRenderer sunRenderer = new XYSeriesRenderer();
        sunRenderer.setColor(Color.YELLOW);
        sunRenderer.setPointStyle(PointStyle.TRIANGLE);
        sunRenderer.setFillPoints(true);
        sunRenderer.setLineWidth(1);
        sunRenderer.setDisplayChartValues(true);


        XYSeriesRenderer rainRenderer = new XYSeriesRenderer();
        rainRenderer.setColor(Color.BLACK);
        rainRenderer.setPointStyle(PointStyle.SQUARE);
        rainRenderer.setFillPoints(true);
        rainRenderer.setLineWidth(1);
        rainRenderer.setDisplayChartValues(true);



        // Creating a XYMultipleSeriesRenderer to customize the whole chart
        XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
        multiRenderer.setXLabels(0);
        multiRenderer.setChartTitle("Measurements Chart");
        multiRenderer.setXTitle("Previous 10 Measurements");
        //multiRenderer.setYTitle("");
        multiRenderer.setZoomButtonsVisible(true);
        multiRenderer.setBarSpacing(4);
        for(int i=0;i<x.length;i++){
            multiRenderer.addXTextLabel(i, iNumber[i]);
        }

        // Adding incomeRenderer and expenseRenderer to multipleRenderer
        // Note: The order of adding dataseries to dataset and renderers to multipleRenderer
        // should be same
        multiRenderer.addSeriesRenderer(temperatureRenderer);
        multiRenderer.addSeriesRenderer(humidityRenderer);
        multiRenderer.addSeriesRenderer(sunRenderer);
        multiRenderer.addSeriesRenderer(rainRenderer);

        // Getting a reference to LinearLayout of the MainActivity Layout
        LinearLayout chartContainer = (LinearLayout) findViewById(R.id.chart_container);


        // Specifying chart types to be drawn in the graph
        // Number of data series and number of types should be same
        // Order of data series and chart type will be same
        String[] types = new String[] { LineChart.TYPE, BarChart.TYPE, LineChart.TYPE, LineChart.TYPE };

        // Creating a combined chart with the chart types specified in types array
        mChart = (GraphicalView) ChartFactory.getCombinedXYChartView(getBaseContext(), dataset, multiRenderer, types);

        multiRenderer.setClickEnabled(true);
        multiRenderer.setSelectableBuffer(10);
        mChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SeriesSelection seriesSelection = mChart.getCurrentSeriesAndPoint();

                if (seriesSelection != null) {
                    int seriesIndex = seriesSelection.getSeriesIndex();
                    String selectedSeries="Temperature";
                    if(seriesIndex==0)
                        selectedSeries = "Temperature";
                    else
                        selectedSeries = "Humidity";
                    // Getting the clicked Month
                    String number = iNumber[(int)seriesSelection.getXValue()];
                    // Getting the y value
                    int amount = (int) seriesSelection.getValue();
                    Toast.makeText(
                            getBaseContext(),
                            selectedSeries + " in "  + number + " : " + amount ,
                            Toast.LENGTH_SHORT).show();
                }
            }

        });

        // Adding the Combined Chart to the LinearLayout
        chartContainer.addView(mChart);
    }


    private void listIterator(List<Measurement> list)
    {
        int i = 0;
        int amount_of_elements = list.size();
        x = new int[amount_of_elements];
        y1 = new int[amount_of_elements];
        y2 = new int[amount_of_elements];
        y3 = new int[amount_of_elements];
        y4 = new int[amount_of_elements];
        for (ListIterator<Measurement> it = list.listIterator(); it.hasNext();)
        {
            // if(i>=9)
            //     break;
            // else
            //{
            //System.out.println(it.nextIndex() + " " + it.next());
            Measurement current = it.next();
            String tempTemp = current.getTemperature();
            String humiHumi = current.getHumidity();
            String sunSun = current.getSun();
            String rainRain = current.getRain();
            int tempTemp2 = Integer.parseInt(tempTemp);
            int humiHumi2 = Integer.parseInt(humiHumi);
            int sunSun2 = booleanStringToInteger(sunSun);
            int rainRain2 = booleanStringToInteger(rainRain);
            x[i] = i;
            y1[i] = tempTemp2;
            y2[i] = humiHumi2;
            y3[i] = sunSun2 * 1000;
            y4[i] = rainRain2 * 1000;
            i++;
            // }
        }
    }



    public void returnToMainActivity(View view)
    {
        Intent i = new Intent(this,MainActivity.class);
        startActivity(i);
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }*/
}