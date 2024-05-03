package com.cafe.cafe_management.utils;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.common.base.Strings;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CafeUtils {

    private CafeUtils(){

    }

   public static ResponseEntity<String> getResponseEntity(String responseMessage,HttpStatus httpStatus){
    return new ResponseEntity<String>("{\"message\":\""+responseMessage+"\"}",httpStatus);
   }


   public static String getUUiD(){
        Date date = new Date();
        long time = date.getTime();
        return "BILL-"+time; 
   }

   public static JsonArray getJsonArrayFromString(String data) throws JsonParseException{
    JsonArray jsonArray = new JsonArray();
    return jsonArray;                                       //may be occur error
   
   }

   public static Map<String , Object> getMapfromJson(String data){
    if(!Strings.isNullOrEmpty(data))
    return new  Gson().fromJson(data, new TypeToken<Map<String , Object>>(){

    }.getType());
    return new HashMap<>();
        
    
   }


   public static Boolean isFileExist(String path){
     log.info("Inside isFileExist{}",path);

     try {
          File file = new File(path);
          return (file !=null && file.exists()) ? Boolean.TRUE : Boolean.FALSE;
     } catch (Exception e) {
          e.printStackTrace();
     }
     return false;
   }

   }
    

