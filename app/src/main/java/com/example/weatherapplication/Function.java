package com.example.weatherapplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class Function {
    //函数getMethod(String url)中的参数url为传入URL链接，
    private static String getMethod(String url)
    {
        String result=null;
        InputStream inputStream = null;
        try {
            //创建URL对象
            URL Url = new URL(url);
            //获得URLConnection类对象，再用URLConnection类对象的connect（）方法进行连接
            HttpURLConnection connection= (HttpURLConnection) Url.openConnection();
            //在HttpURLConnection里setDoInput(true)一般设置为true，因为需要接收数据，而setDoOutput(false)默认是false，因为get方法中参数都包含在url链接中
            connection.setDoInput(true);
            //setRequestMethod("GET")设置请求方法为Get方法
            connection.setRequestMethod("GET");
            //与服务器构建链接
            connection.connect();
            //获取输入流
            inputStream = connection.getInputStream();
            //读取byte数据
            result= readfile(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(inputStream!=null)
                //关闭输入流
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static String readfile(InputStream input) throws IOException {
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream readline= new ByteArrayOutputStream();
        int length; //当前读的字节数的长度
        while((length=input.read(buffer))>-1){
            readline.write(buffer,0,length);

        }
        String result= readline.toString();
        return result;
    }

    //函数postMethod(String url,String jsdata)中的参数url为传入URL链接，jsdata是json参数
    private static String postMethod(String url,String jsdata)
    {
        String result=null;
        try {
            URL Url = new URL(url);
            HttpURLConnection connection= (HttpURLConnection) Url.openConnection();
            //允许构建输出流
            connection.setDoOutput(true);
            //允许构建输入流
            connection.setDoInput(true);
            connection.setUseCaches(false);
            //设置请求为Post方法
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            //设置编码方式为UTF-8
            connection.setRequestProperty("Charset", "UTF-8");
            //数据格式可以根据自己需要设置，这里采用的是json数据格式
            connection.setRequestProperty("contentType", "application/json");
            byte[] data = jsdata.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.connect();
            //将数据上传到服务器
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(data);
            out.flush();
            out.close();
            //判断是否响应成功，成功则读取数据
            if (HttpURLConnection.HTTP_OK == connection.getResponseCode()){
                InputStream inputStream = connection.getInputStream();
                byte[] bytes=new byte[inputStream.available()];
                inputStream.read(bytes);
                result = new String(bytes,"utf-8");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }


    private static List<String> parselocation(String data){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        JsonObject result= jsonObject.get("results").getAsJsonArray().get(0).getAsJsonObject().get("geometry").getAsJsonObject().get("location").getAsJsonObject();


        String latitude=result.get("lat").getAsString();
        String longitude=result.get("lng").getAsString();

        List<String> location= new ArrayList<>();
        location.add(latitude);
        location.add(longitude);
        return location;
    }

    public static List getLocation(String place){
        String result=getMethod("https://maps.googleapis.com/maps/api/geocode/json?address="+place+"&key=AIzaSyDybcUf34eT2jk6VtlxEK1jSg4zeNQdc2k");
        List location=parselocation(result);
        return location;
    }

    private static List parseweather(String data){
        JsonParser jsonParser = new JsonParser();
        JsonObject jsonObject = jsonParser.parse(data).getAsJsonObject();
        JsonObject result= jsonObject.get("currently").getAsJsonObject();

        String temperature=result.get("temperature").getAsString();
        String humidity=result.get("humidity").getAsString();
        String windspeed=result.get("windSpeed").getAsString();
        String precipintensity=result.get("precipIntensity").getAsString();
        String precipprob=result.get("precipProbability").getAsString();
        String summary=result.get("summary").getAsString();

        List<String> weather=new ArrayList<>();
        weather.add(temperature);
        weather.add(humidity);
        weather.add(windspeed);
        weather.add(precipintensity);
        weather.add(precipprob);
        weather.add(summary);

        //String weathercon=temperature+","+humidity+","+windspeed+","+precipintensity+","+precipprob;
        return weather;
    }


    public static List getWeather(String location){
        String result= getMethod("https://api.darksky.net/forecast/c016091c54babff913bc4021060099cc/"+location);
        List weather= parseweather(result);
        return weather;
    }

}

