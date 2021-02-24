import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    public static void main(String[] args) throws IOException {

        ArrayList<Vehicle> list = new ArrayList<>();
        BigDecimal listPrice = BigDecimal.ZERO;

        //Getting inputs
        File csvFile = getFile();
        BigDecimal taxRate = getTaxRate();

        try {
            Scanner scanner = new Scanner(csvFile);
            String headerLine = scanner.nextLine();
            String[] header = headerLine.split(",");

            if(header[0].toLowerCase().compareTo("year") !=0
                    || header[1].toLowerCase().compareTo("make") != 0
                    || header[2].toLowerCase().compareTo("model") !=0
                    || header[3].toLowerCase().compareTo("msrp") !=0)
            {
                errorPopup("Headers of the csv file are incorrect","Error");
                System.exit(1);
            }

            if(header.length > 4)
            {
                errorPopup("Please check formatting of the csv file","Error");
                System.exit(1);

            }

            while(scanner.hasNextLine())
            {
                String line = scanner.nextLine();
                String [] vehicleInfo = line.split(",");
                int year = Integer.parseInt(vehicleInfo[0]);
                String make = vehicleInfo[1];
                String model = vehicleInfo[2];
                BigDecimal msrp = new BigDecimal(vehicleInfo[3]);
                Vehicle car = new Vehicle(year,make,model,msrp);
                list.add(car);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // sorting the cars by Year
        Collections.sort(list,Vehicle.YearComparator);

        // sum of call cars MSRP.
        for(Vehicle v : list )
        {
            listPrice=listPrice.add(v.getMsrp());
        }

        HashMap<Integer, Vehicle> map  = new HashMap<>();

        for(Vehicle v : list )
        {
            map.put(v.year,v);

        }

        Iterator<Integer> itr = map.keySet().iterator();
        ArrayList<Integer> listOfYear = new ArrayList<>();

        while(itr.hasNext())
        {
            listOfYear.add(itr.next());
        }
        //Gathering a list of Years and also sorting by Make.
        Collections.sort(listOfYear);
        ArrayList<Vehicle> listSortedByMake = new ArrayList<>(list);
        Collections.sort(listSortedByMake,Vehicle.MakeComparator);
        String newLine = System.getProperty("line.separator");

        Date date = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("MMddyyyy");
        String strDate= formatter.format(date);

        Date reportedDate = new Date();
        SimpleDateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
        String reportDate= formatter2.format(reportedDate);


        //Generating the txt file for the report
        String parentDir = csvFile.getParent();

        FileWriter writer = new FileWriter(new File(parentDir).getAbsolutePath()+"/vehicles"+strDate+".txt",
                false);


        writer.write("--- Vehicle Report ---" + "\t\t\t\t\t\t\t\t"+ "Date: "+ reportDate +"\n");

        for(Integer i : listOfYear)
        {
            writer.write(i +"\n");

            for(Vehicle v : listSortedByMake)
            {
                if(v.year == i )
                {
                    //Printing car Information
                    String year = String.format("%-4d",v.year);
                    String make = String.format("%-10s",v.make);
                    String model = String.format("%-15s",v.model);
                    String msrp = String.format("%.2f",v.msrp);
                    String listPrice2 = String.format("%.2f",v.msrp.multiply(taxRate));

                   writer.write(("\t"
                           + year
                           + " "
                           + make
                           + " "
                           + model
                           + " MSRP:$"
                           + msrp
                           + "\t"
                           + "List Price:$"
                           + listPrice2
                           + "\n"));

                }
            }
            writer.write(newLine);
        }

        //Prints totals for the report
        writer.write("---Grand Total---\n");
        String totalListPrice = String.format("%.2f",listPrice);
        writer.write("\t"+"MSRP:$" + totalListPrice + "\n");
        writer.write("\t"+"List Price:$"+ listPrice.multiply(taxRate));
        writer.write("\n");
        writer.close();
    }

    /**
     * This method is used to generate pop up for errors.
     * @param infoMessage
     * @param titleBar
     */
    private static void errorPopup(String infoMessage, String titleBar)
    {
        JOptionPane.showMessageDialog(null,infoMessage,"InfoBox: " + titleBar,
                JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * This is create a file Dialog inorder to select the csv file.
     * @return  csv File
     */
    private static File getFile()
    {
        JFileChooser fc = new JFileChooser();
        FileNameExtensionFilter csvFilter = new FileNameExtensionFilter("CSV File" , "csv");
        fc.setFileFilter(csvFilter);
        File file = null;
        int returnVal = fc.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION )
        {
            file=fc.getSelectedFile();
        }
        return file;
    }

    /**
     * This method is used to create a dialog box which prompts the user for a tax rate.
     * @return taxRate
     */
    private static BigDecimal getTaxRate()
    {
        String taxRateInputString = JOptionPane.showInputDialog(null,
                "Enter a tax rate",
                "Vehicles Report Generator",
                JOptionPane.QUESTION_MESSAGE);
        try
        {
            BigDecimal taxRate = new BigDecimal(taxRateInputString);
            return taxRate;

        }catch(NumberFormatException e){
            errorPopup("Tax rate must be a number","error");
            System.exit(1);
        }
        return BigDecimal.ZERO;
    }

}
