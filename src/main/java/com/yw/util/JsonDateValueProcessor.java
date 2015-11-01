package com.yw.util;

import java.text.SimpleDateFormat;  
import java.util.Date;  
import java.util.Locale;  
  
import net.sf.json.JsonConfig;  
import net.sf.json.processors.JsonValueProcessor;  
  
public class JsonDateValueProcessor implements JsonValueProcessor {  
      
    private SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
      return  value == null ?"" : sd.format(value);
    }
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
      return null;
    }
}  
