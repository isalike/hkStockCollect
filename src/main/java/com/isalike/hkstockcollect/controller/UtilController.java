package com.isalike.hkstockcollect.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value="/")
public class UtilController {
    @RequestMapping(value="/test")
    public String main(){
        return "123";
    }
}
