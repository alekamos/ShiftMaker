package it.costanza.shiftgenerator.command;

import it.costanza.shiftgenerator.model.Const;
import it.costanza.shiftgenerator.model.ShiftCalendar;
import it.costanza.shiftgenerator.model.Worker;
import it.costanza.shiftgenerator.service.ExcelAvisLoaderService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@Component
@Scope("prototype")
public class ShiftCommand implements ICommand {

    Logger log = LoggerFactory.getLogger(ShiftCommand.class);

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

        File file = null;
        try {

            file =  multipartFileToFile(inputData);




            switch (type) {
                case Const.TYPE_SHIFT_AVIS:
                    Map<String, String> slotMap = excelService.loadSlotDescription(file);
                    List<Worker> workers = excelService.loadWorkers(file,slotMap);
                    ShiftCalendar shiftCalendar = excelService.loadShiftCalendar(file);


                    workers.forEach(worker -> log.info("worker: {}",worker));
                    log.info("Shift Calendar {}",shiftCalendar.toString());


            }
        } catch (IOException | InvalidFormatException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if(file!=null) {
                boolean delete = file.delete();
                if(!delete)
                    log.warn("file not deleted");
            }
        }
    }



    private File multipartFileToFile(MultipartFile multipart) throws IOException {
        Path filepath = Paths.get("D:\\cross_os\\progetti\\shiftGenerator\\tmpDir", multipart.getOriginalFilename());
        multipart.transferTo(filepath);
        return new File("D:\\cross_os\\progetti\\shiftGenerator\\tmpDir\\"+multipart.getOriginalFilename());
    }







}
