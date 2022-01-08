package it.costanza.shiftgenerator.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;



@RestController
public class ShiftController {





    @ApiOperation(value = "Generate shift calendar", notes = "Generate shift calendar by type of calendar and input data (excel see example)", tags={ "Operation" })
    @RequestMapping(value = "/generate", method = RequestMethod.POST)
    public String custom(
            @ApiParam(
                    name =  "inputData",
                    type = "File",
            value = "excel contains data for generation, names, resources, calenadar, resource availability etc ")
            @RequestParam File excelFile,
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
        return "yeah!";
    }
}

