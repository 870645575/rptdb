package com.ldw.Controler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import com.alibaba.fastjson.JSONObject;

public class Map_API {
    private static String ak="MebLqCVk3iYOGlpB0Le7frVtV5nrh3Pd";
    public static String[] getCoordinate(String addr) throws IOException {   
        String lng = null;
        String lat = null;
        String address = "";   
        try {   
            address = java.net.URLEncoder.encode(addr, "UTF-8");   
        }catch (UnsupportedEncodingException e1) {   
            e1.printStackTrace();   
        }   
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak="+ak+"&address="+address;  
        URL myURL = null;   
        URLConnection httpsConn = null;   
        try {  
            myURL = new URL(url);   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        }   
        InputStreamReader insr = null;  
        BufferedReader br = null;  
        try {   
            httpsConn = (URLConnection) myURL.openConnection();  
            if (httpsConn != null) {   
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");   
                br = new BufferedReader(insr);   
                String data = null;   
                while((data= br.readLine())!=null){   
                	JSONObject json = JSONObject.parseObject(data); 
                	int state=json.getIntValue("status");
                    if (state==0) {
                    	 lng = json.getJSONObject("result").getJSONObject("location").getString("lng");  
                         lat = json.getJSONObject("result").getJSONObject("location").getString("lat"); 
					} else {
						System.out.println(data);
					}                  
                }  
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {  
            if(insr!=null){  
                insr.close();  
            }  
            if(br!=null){  
                br.close();  
            }  
        }  
        return new String[]{lng,lat};   
    }   
  
    public static String[] getAddr(String lng,String lat) throws IOException {   
  
        String url = "http://api.map.baidu.com/geocoder/v2/?output=json&ak="+ak+"&output=json&location="+lat+","+lng;  
        URL myURL = null;   
        String city = "";  
        String qx = "";  
        URLConnection httpsConn = null;   
        try {  
            myURL = new URL(url);   
        } catch (MalformedURLException e) {   
            e.printStackTrace();   
        }   
        InputStreamReader insr = null;  
        BufferedReader br = null;  
        try {   
            httpsConn = (URLConnection) myURL.openConnection();
            if (httpsConn != null) {   
                insr = new InputStreamReader( httpsConn.getInputStream(), "UTF-8");   
                br = new BufferedReader(insr);   
                String data = null;   
                while((data= br.readLine())!=null){
                	//System.out.println(data);
                    JSONObject json = JSONObject.parseObject(data);  
                    int state=json.getIntValue("status");
                    if (state==0) {
                    	 city = json.getJSONObject("result").getJSONObject("addressComponent").getString("city");  
                         qx= json.getJSONObject("result").getJSONObject("addressComponent").getString("district");
                         if (city.equals("")&&qx.equals("")) {
							System.out.println("未查询到该地区");
							city=null;
							qx=null;
						}
					} else {
						System.out.println( data);
					}
                     
                }  
            }   
        } catch (IOException e) {   
            e.printStackTrace();   
        } finally {  
            if(insr!=null){  
                insr.close();  
            }  
            if(br!=null){  
                br.close();  
            }  
        }  
        return new String[]{city,qx};   
    }    
	public static boolean Cord_check(String string) {
		boolean s=false;
		String [] o= string.split(",");
		if (o.length<2) {
			return s;
		}
		try {
			System.out.println(o[0]+"    "+o[1]);
			String[] str=Map_API.getAddr(o[0], o[1]); 
			if (str[0]!=null&&str[1]!=null) {
				s=true;
			} 
		} catch (IOException e) {
			e.printStackTrace();
		} 
		return s;
	}
 

}
