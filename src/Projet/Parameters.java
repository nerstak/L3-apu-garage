package Projet;

import java.io.File;
import java.io.PrintWriter;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;

/**
 * Class that holds parameters of program
 */
public class Parameters {
    // Saved in hard code
    private static final String _configFile = "parameters/config.txt";
    private DateTimeFormatter _dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH");
    private final DateTimeFormatter _dateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    // Saved in file
    private Double _priceNormal;
    private Double _priceMajor;
    private Integer _appointmentIndex;
    private Integer _userIndex;

    public Parameters() {
        try {
            Scanner s = new Scanner(new File(_configFile));

            _priceNormal = Double.valueOf(s.nextLine());
            _priceMajor = Double.valueOf(s.nextLine());
            _userIndex = Integer.valueOf(s.nextLine());
            _appointmentIndex = Integer.valueOf(s.nextLine());
            s.close();
        } catch (Exception e) {
        }
    }

    /**
     * Write parameters in file
     */
    private void writeParameters() {
        try {
            PrintWriter p = new PrintWriter(_configFile);
            p.println(_priceNormal);
            p.println(_priceMajor);
            p.println(_userIndex);
            p.println(_appointmentIndex);
            p.close();
        } catch (Exception e) {
        }
    }

    public DateTimeFormatter getDateFormat() {
        return _dateFormat;
    }

    public Double getPriceNormal() {
        return _priceNormal;
    }

    public Double getPriceMajor() {
        return _priceMajor;
    }

    /**
     * Set normal price
     * @param _price Double price
     */
    public void setPriceNormal(Double _price) {
        this._priceNormal = _price;
        writeParameters();
    }

    /**
     * Set major price
     * @param _price Double price
     */
    public void setPriceMajor(Double _price) {
        this._priceMajor = _price;
        writeParameters();
    }

    /**
     * Get number of user created
     * @return Integer
     */
    public Integer getNumberUserCreated() {
        if(_userIndex == 0) {
            return 0;
        }
        return _userIndex - 1;
    }

    /**
     * Get future index for new appointment
     * @return Integer Index
     */
    public Integer newAppointmentIndex() {
        Integer tmp = _appointmentIndex++;
        writeParameters();
        return tmp;
    }

    /**
     * Get future index for new user
     * @return Integer Index
     */
    public Integer newUserIndex() {
        Integer tmp = _userIndex++;
        writeParameters();
        return tmp;
    }
}
