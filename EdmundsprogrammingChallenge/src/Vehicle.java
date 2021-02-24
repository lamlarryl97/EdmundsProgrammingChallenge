import java.math.BigDecimal;
import java.util.Comparator;

public class Vehicle {
    public int year;
    public String make;
    public String model;
    public BigDecimal msrp;

    //Constructor
    public Vehicle(int year, String make, String model, BigDecimal msrp)
    {
        this.year = year;
        this.make = make;
        this.model = model;
        this.msrp = msrp;
    }

    /**
     * This is a method used to compare the years in the Vehicle Class.
     */
    public static Comparator<Vehicle> YearComparator = new Comparator<Vehicle>() {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {

            int year1 = v1.getYear();
            int year2 = v2.getYear();
            return  year1-year2;
        }
    };
    /**
     * This is a method used to compare the make in the Vehicle Class.
     */
    public static Comparator<Vehicle> MakeComparator = new Comparator<Vehicle>() {
        @Override
        public int compare(Vehicle v1, Vehicle v2) {
            String make1 = v1.getMake();
            String make2 = v2.getMake();
            return make1.compareTo(make2);
        }
    };

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public BigDecimal getMsrp() {
        return msrp;
    }

    public void setMsrp(BigDecimal msrp) {
        this.msrp = msrp;
    }
}
