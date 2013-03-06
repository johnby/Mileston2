package JChart;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

public class DemoChart {

  public static void main(String[] args) {

    DefaultPieDataset data = new DefaultPieDataset();
                      data.setValue("Category 1", 5);
                      data.setValue("Category 2", 10);
                      data.setValue("Category 3", 85);

    JFreeChart        chart = ChartFactory.createPieChart("Sample Pie Chart",data,true,true,false);

    ChartFrame        frame = new ChartFrame("First", chart);
                      frame.pack();
                      frame.setVisible(true);
  }
}