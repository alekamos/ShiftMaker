package service;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

public class  PropertiesServices {




    /**
     * Mi prende la properties dal file delle properties
     * @param name
     * @return
     * @throws IOException
     */
    public static String getProperties(String name) throws IOException {

        return getProperties(name,null);
    }



    /**
     * Mi prende la properties dal file delle properties specificando il file di properties, se propfile name nullo, mi prende quello di default
     * @param name
     * @return
     * @throws IOException
     */
    public static String getProperties(String name,String propFileName) throws IOException {

        String output = "";

        Properties prop = new Properties();
        String defaultPropFile = "config.properties";

        if(propFileName==null)
            propFileName = defaultPropFile;


        InputStream inputStream = PropertiesServices.class.getClassLoader().getResourceAsStream(propFileName);

        if (inputStream != null) {
            prop.load(inputStream);
        } else {
            throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
        }



        return prop.getProperty(name);
    }


    public static Integer getAnno() throws IOException {
        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");


        Workbook workbook = new XSSFWorkbook(file);
        Sheet foglio_0 = workbook.getSheetAt(0);
        Date dateCellValue = foglio_0.getRow(0).getCell(1).getDateCellValue();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCellValue);
        return cal.get(Calendar.YEAR);


    }


    public static Integer getMese() throws IOException {
        FileInputStream file = new FileInputStream("commonFiles/dati.xlsx");


        Workbook workbook = new XSSFWorkbook(file);
        Sheet foglio_0 = workbook.getSheetAt(0);
        Date dateCellValue = foglio_0.getRow(0).getCell(1).getDateCellValue();

        Calendar cal = Calendar.getInstance();
        cal.setTime(dateCellValue);
        return cal.get(Calendar.MONTH)+1;


    }

}
