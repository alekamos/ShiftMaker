package it.costanza.shiftgenerator.command;

import model.Const;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;
import service.ExcelAvisLoaderService;
import service.IExcelService;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class ShiftCommand implements ICommand{

    @Autowired
    ExcelAvisLoaderService excelService;


    private MultipartFile inputData;
    private String type;
    private String notificationEmail;

    public ShiftCommand(MultipartFile inputData, String type, String notificationEmail) {
        this.inputData = inputData;
        this.type = type;
        this.notificationEmail = notificationEmail;
    }

    public void execute() {


        try {
            multipartFileToFile(inputData);
            File file = getFile();


            switch (type) {
                case Const.TYPE_SHIFT_AVIS:


                    excelService.loadWorkers(file);


            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        }
    }

    private void multipartFileToFile(MultipartFile multipart) throws IOException {
        Path filepath = Paths.get("D:\\cross_os\\progetti\\shiftGenerator\\tmpDir", multipart.getOriginalFilename());
        multipart.transferTo(filepath);
    }

    private File getFile() throws IOException {
        return new File("D:\\cross_os\\progetti\\shiftGenerator\\tmpDir");
    }


}
