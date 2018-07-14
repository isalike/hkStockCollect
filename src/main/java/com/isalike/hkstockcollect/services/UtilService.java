package com.isalike.hkstockcollect.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class UtilService {
    private ObjectMapper objectMapper = new ObjectMapper();
    public String test(String index){
        String resultHTML = CustomHttpClient.doGet("http://www.aastocks.com/tc/mobile/Quote.aspx?symbol="+index);
        if(resultHTML.contains("Images/down_arrow.png?v=1.1")){
            return index + "||" + resultHTML.substring(resultHTML.indexOf("style=\"border:0px;\" /><span class=\"neg bold\">")+45,resultHTML.indexOf("<div style=\"position:relative; top:5px; left:5px;\">")-25);
        }else if(resultHTML.contains("Images/up_arrow.png?v=1.1")){
            return index + "||" + resultHTML.substring(resultHTML.indexOf("style=\"border:0px;\" /><span class=\"pos bold\">")+45,resultHTML.indexOf("<div style=\"position:relative; top:5px; left:5px;\">")-25);
        }else{
            int start = resultHTML.indexOf("<span class=\"unc bold\">")+23;
            return index + "||" + resultHTML.substring(start,start+resultHTML.substring(start).indexOf(".")+3);
        }
    }

    public String test2(String index,String startDt,String endDt) throws Exception{
        while(index.length()<5){
            index = 0 + index;
        }
        //String resultHTML = CustomHttpClient.doGet("https://www.quandl.com/api/v3/datasets/HKEX/"+index+"?start_date="+startDt+"&end_date="+endDt+"&api_key=YPPWmoxrefDGxKVfKyuv");
        Document docs = Jsoup.connect("https://www.quandl.com/api/v3/datasets/HKEX/"+index+"?start_date="+startDt+"&end_date="+endDt+"&api_key=YPPWmoxrefDGxKVfKyuv").get();
        Element a = docs.select("pre code").first();
        String temp = a.childNode(0).toString().replace("\n","").replace("{  \"dataset\": ","");
        HashMap<String,Object> result =
                new ObjectMapper().readValue(temp.substring(0,temp.length()-1), HashMap.class);
        //result = new ObjectMapper().readValue(result.get("dataset").toString(), HashMap.class);

        /*String result = resultHTML.substring(resultHTML.indexOf("<code data-language=\"ruby\">")+27,resultHTML.indexOf("</code>")).replace("\n","");
        Map<String,String> aMap = null;
        try {
            aMap = objectMapper.readValue(result,new TypeReference<Map<String, String>>(){});
        } catch (IOException e) {
            e.printStackTrace();
        }
        return aMap.toString();*/

        return result.get("data").toString();
    }
}
