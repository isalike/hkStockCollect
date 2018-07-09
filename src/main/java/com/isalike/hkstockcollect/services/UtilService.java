package com.isalike.hkstockcollect.services;

import org.springframework.stereotype.Service;

@Service
public class UtilService {
    public String test(String index){
        String resultHTML = CustomHttpClient.doGet("http://www.aastocks.com/tc/mobile/Quote.aspx?symbol="+index);
        if(resultHTML.contains("Images/down_arrow.png?v=1.1")){
            return index + "||" + resultHTML.substring(resultHTML.indexOf("style=\"border:0px;\" /><span class=\"neg bold\">")+45,resultHTML.indexOf("<div style=\"position:relative; top:5px; left:5px;\">")-25);
        }else{
            return index + "||" + resultHTML.substring(resultHTML.indexOf("style=\"border:0px;\" /><span class=\"pos bold\">")+45,resultHTML.indexOf("<div style=\"position:relative; top:5px; left:5px;\">")-25);
        }
    }
}
