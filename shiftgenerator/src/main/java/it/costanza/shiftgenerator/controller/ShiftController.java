package it.costanza.shiftgenerator.controller;

import io.swagger.annotations.*;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import it.costanza.shiftgenerator.command.ShiftCommand;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;


@RestController
public class ShiftController {





    @ApiOperation(value = "Generate shift calendar", notes = "Generate shift calendar by type of calendar and input data (excel see example)", tags={ "Operation" })
    @RequestMapping(value = "/generate", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String custom(

            @ApiParam(value="excel data",type = "file") @RequestParam MultipartFile excelFile,

            @ApiParam(
                    name =  "type",
                    type = "String",
                    example = "AVIS_1",
                    value = "type of shift",
                    allowableValues = "AVIS_1,RIA_1",
                    defaultValue = "AVIS_1")
            @RequestParam String type,
            @ApiParam(
                    name =  "notificationEmail",
                    type = "String",
                    value = "email for recive notification for long time processing"
                    )
            @RequestParam(required = false)  String mail
            )

    {

        ShiftCommand command = new ShiftCommand(excelFile,type,mail);
        command.execute();

        return "yeah!";
    }
}

