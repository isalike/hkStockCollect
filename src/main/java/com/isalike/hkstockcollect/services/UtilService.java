package com.isalike.hkstockcollect.services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.isalike.hkstockcollect.constant.Stock;
import com.isalike.hkstockcollect.dao.MariaDbMasterFactory;
import com.isalike.hkstockcollect.dao.StockDAO;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service
public class UtilService {
    private ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private StockDAO stockDAO;
    @Autowired
    private MariaDbMasterFactory mariaDbMasterFactory;
    @Autowired
    private JdbcTemplate jdbcTemplate;

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

    public String getData(String index,String startDt,String endDt) throws Exception{
        while(index.length()<5){
            index = 0 + index;
        }
        Document docs = Jsoup.connect("https://www.quandl.com/api/v3/datasets/HKEX/"+index+"?start_date="+startDt+"&end_date="+endDt+"&api_key=YPPWmoxrefDGxKVfKyuv").get();
        Element a = docs.select("pre code").first();
        String temp = a.childNode(0).toString().replace("\n","").replace("{  \"dataset\": ","");
        HashMap<String,Object> result =
                new ObjectMapper().readValue(temp.substring(0,temp.length()-1), HashMap.class);
        return insertData(index,result.get("data").toString());
    }

    public String insertData(String symbol,String data){
        String[] result = data.substring(1,data.length()-1).split("],");
        ArrayList<ArrayList<String>> aList = new ArrayList<ArrayList<String>>();
        for(int i = 0;i < result.length ; i++){
            aList.add(new ArrayList<String>(Arrays.asList(result[i].replace("[","").replace("]","").split(","))));
        }
        for(int i = 0 ; i < aList.size() ; i++){
            try{
                String sql = "INSERT INTO daily_record(symbol,recordDt,closeValue,lastValue,dayHighValue,dayLowValue) VALUES ("+symbol+",'"+aList.get(i).get(0)+"',"+aList.get(i).get(1)+","+aList.get(i).get(9)+"," +
                        aList.get(i).get(7)+","+aList.get(i).get(8) +");";
                jdbcTemplate.execute(sql);
            }catch (Exception e){
            }
        }
        return "ok";
    }
}
