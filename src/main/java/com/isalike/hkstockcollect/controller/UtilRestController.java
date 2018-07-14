package com.isalike.hkstockcollect.controller;

import com.isalike.hkstockcollect.services.UtilService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class UtilRestController {
    @Autowired
    private UtilService utilService;
    @RequestMapping(value="/test")
    public String test(@RequestParam(value = "index", required = true) String index){
        return utilService.test(index);
    }
/*
    @RequestMapping(value="/testing")
    public String testing(){
        return "<3 u";
    }
*/
    @RequestMapping(value="/test2")
    public String test2(@RequestParam(value = "index", required = true) String index,
                        @RequestParam(value = "startDt", required = true) String startDt,
                        @RequestParam(value = "endDt", required = true) String endDt) throws Exception{
        //return "";
        return utilService.test2(index,startDt,endDt);
    }
}
