package org.openpaas.ieda.azureMgnt;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.fasterxml.jackson.dataformat.yaml.snakeyaml.Yaml;

public class App {
    public static void main(String[] args) {
        String manifestTemplateFile = "/home/min/OPENPAAS-IEDA-WEB-v3.0/OPENPAAS-IEDA-CONTROLLER/src/main/resources/static/deploy_template/cf-deployment/azure/test.yml";
        FileInputStream fis = null;
        BufferedReader rd = null;
        try {
            fis = new FileInputStream(new File(manifestTemplateFile));
            rd = new BufferedReader(new InputStreamReader(fis,"UTF-8"));
            String line = null;
            String contents = "";
            while((line = rd.readLine()) != null) {
                contents += line + "\n";
            }
            Yaml yaml = new Yaml();
            Map<String, Object> object = (Map<String, Object>)yaml.load(contents);
            JSONObject jObj = new JSONObject(object);
            Iterator<?> i = jObj.keySet().iterator();
            List<String> jsonKeyList = new ArrayList<String>();
            while(i.hasNext()){
                String key = i.next().toString();
                //System.out.println(key);
                jsonKeyList.add(key);
            }
            
            blockLevel1(jsonKeyList, jObj);
            
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    //얘는 당연히 있나?
    private static void blockLevel1(List<String> jsonKeyList, JSONObject jObj) {
        if(jsonKeyList != null && jsonKeyList.size() != 0){
            JSONParser jsonParser = new JSONParser();
            JSONArray jarray = new JSONArray();
            JSONObject jsonObject= null;
            System.out.println(jObj.toJSONString());
            for(int i=0; i< jsonKeyList.size(); i++){
                System.out.println(jsonKeyList.get(i)); //넘어 온 키 값 출력
                System.out.println(jObj.get(jsonKeyList.get(i)));
                JSONArray bookInfoArray = (JSONArray) jObj.get("networks");
            }
        }
    }
    //block 2 name, type, subnets
    @SuppressWarnings("unchecked")
    private static void blockLevel2(String value) {
        String[] existBlockLevel2 = {"name, type, subnets"};
        JSONObject obj = new JSONObject();
        JSONArray jarray = new JSONArray();
        jarray.add(value);
        for(int i = 0; i < jarray.size(); i++) {
            obj = (JSONObject) jarray.get(i);
            System.out.println(obj.get("name"));
            System.out.println(obj.get("type"));
            System.out.println(obj.get("subnets"));
        }
    }
}
