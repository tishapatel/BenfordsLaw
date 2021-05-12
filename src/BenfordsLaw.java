import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class BenfordsLaw {

    public static void main(String[] args) throws FileNotFoundException {
        // Read sales numbers from sales.csv in project
        // Calculate frequencies which will be array where index is digit and value is
        // frequency of that digit (%)
        ArrayList<String> sales = readSalesFromCsvFile();
        double[] frequencies = calculateFrequencies(sales);

        // Numeric and visual representation of frequencies of digits from 1 - 9 from
        // sales
        printNumericRepresentation(frequencies);
        createVisualRepresentation(frequencies);

        // States whether fraud did or did not occur
        if (didNoFraudOccur(frequencies)) {
            System.out.println("The data indicates that fraud likely did not occur.");
        } else {
            System.out.println("The data indicates that fraud did occur.");
        }

        // Output frequencies to csv
        outputToCsv(frequencies);
    }

    private static void outputToCsv(double[] frequencies) throws FileNotFoundException {
        File fileOut = new File("results.csv");

        // java utils allows write to file
        PrintWriter out = new PrintWriter(fileOut);
        // fist line
        String columnsName = "Digits,Frecency(%)\n";
        out.write(columnsName);
        for (int i = 1; i < frequencies.length; i++) {
            // Set value of digit i (1 - 9) -> frequency percentage of i in csv file
            String line = Integer.toString(i) + "," + Double.toString(frequencies[i]) + "\n";
            out.write(line);
        }

        out.close();

    }

    /**
     * @return ArrayList<String>
     * @throws FileNotFoundException
     */
    public static ArrayList<String> readSalesFromCsvFile() throws FileNotFoundException {
        ArrayList<String> salesDataArray = new ArrayList<String>();

        // open file sales.csv
        File salesFile = new File("sales.csv");
        if (!salesFile.exists()) {
            throw new FileNotFoundException("The sales file does not find.");
        }

        // make scanner for read from sales file
        Scanner readerFile = new Scanner(salesFile);
        readerFile.useDelimiter(",");
        // returns a boolean value
        while (readerFile.hasNext()) {
            String data = readerFile.next();
            // check if data is not empty
            if (!data.isEmpty()) {
                // check if string is number
                if (Character.isDigit(data.charAt(0))) {
                    // add to array
                    salesDataArray.add(data);
                }
            }
        }

        readerFile.close();

        return salesDataArray;
    }

    /**
     * @param str
     * @return boolean check strin if it is numeric
     */
    public static boolean isNumeric(String str) {
        try {
            Long.parseLong(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /***
     * Takes sales that were read in from .csv and will calculate the first digit
     * frequencies of those sales as a percentage of all digits. The resulting
     * double[] will be digit -> frequency. Example: frequencies[1] is the frequency
     * of first digit being 1.
     *
     * @param sales that were read in from sales.csv
     * @return double[] the frequencies calculated from sales
     */
    public static double[] calculateFrequencies(ArrayList<String> sales) {
        double[] occurrences = new double[10];
        double[] frequencies = new double[10];

        // Calculate the occurrencies of each first digit (1 - 9)
        for (int i = 0; i < sales.size(); i++) {
            int firstDigit = Character.getNumericValue(sales.get(i).charAt(0));
            occurrences[firstDigit] += 1;
        }

        // Calculate the frequencies of each first digit (1 - 9) which is occurrences /
        // total
        for (int i = 1; i < occurrences.length; i++) {
            frequencies[i] = (occurrences[i] / sales.size()) * 100;
        }

        return frequencies;
    }

    /**
     * Prints out the sales first digit frequency percentages in a numeric
     * representation
     * 
     * @param frequencies is the sales firist digit frequencies
     * @return void
     */
    public static void printNumericRepresentation(double[] frequencies) {
        System.out.println("digit,frequency (%)");
        for (int i = 1; i < frequencies.length; i++) {
            System.out.println(i + "," + frequencies[i]);
        }
    }

    /**
     * Creates a visual representation (bar chart) of the sales first digit
     * frequency precentages
     * 
     * @param frequencies is the sales firist digit frequencies
     * @return void
     */
    public static void createVisualRepresentation(double[] frequencies) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 1; i < frequencies.length; i++) {
            // Set value of digit i (1 - 9) -> frequency percentage of i in dataset
            dataset.setValue(frequencies[i], "Frequency", Integer.toString(i));
        }

        // Names the bar graph according to the frequency, digits and name
        JFreeChart chart = ChartFactory.createBarChart("Benfords Law Distribution Leading Digit", "Digits", "Frequency",
                dataset, PlotOrientation.VERTICAL, true, true, false);

        // Create chart panel with the bar graph
        ChartPanel chartPanel = new ChartPanel(chart);
        chartPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chartPanel.setBackground(Color.white);

        // Create JFrame and add chart panel to it, closing it does not terminal
        // program, then set it to be visible
        JFrame frame = new JFrame();
        frame.add(chartPanel);
        frame.pack();
        frame.setTitle("Visual Representation of Sales Frequencies");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setVisible(true);
    }

    /**
     * @param frequencies is the sales firist digit frequencies
     * @return boolean true if fraud likely did not occur or false if it did occur
     */
    public static boolean didNoFraudOccur(double[] frequencies) {
        // Fraud likely did not occur if first digit frequency is between 29 and 32
        double firstDigitFrequency = frequencies[1];
        return firstDigitFrequency >= 29 && firstDigitFrequency <= 32;
    }
}
