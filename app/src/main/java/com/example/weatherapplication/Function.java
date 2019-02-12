package com.example.weatherapplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

//Siyu driving now
public class Function {
    private static String getMethod(String url)
    {
        String result=null;
        InputStream inputStream = null;
        try {
            //create an URL object
            URL Url = new URL(url);
            //get the object of the URLConnection class，
            //use the connect() method of the  URLConnection class to connect
            HttpURLConnection connection= (HttpURLConnection) Url.openConnection();
            //Inside HttpURLConnection, the input argument of setDoInput(true) normally set as true，
            // as it needs to receive data
            // but the default input argument of setDoOutput(false) is false，
            //as the argument of the get method is contained in the url
            connection.setDoInput(true);
            //setRequestMethod("GET"): set the request method as Get method
            connection.setRequestMethod("GET");
            //get connection with the server
            connection.connect();
            //get the input stream
            inputStream = connection.getInputStream();
            //read byte data
            result= readfile(inputStream);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                if(inputStream!=null)
                //close input stream
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }  //end of Siyu driving, Chengjing driving now

    private static String readfile(InputStream input) throws IOException {
        byte[] buffer = new byte[2048];
        ByteArrayOutputStream readline= new ByteArrayOutputStream();
        int length; //the length of currently read bytes
        while((length=input.read(buffer))>-1){
            readline.write(buffer,0,length);

        }
        String result= readline.toString();
        return result;
    }

    //another method, postMethod(String url,String jsdata), we don't use here
    private static String postMethod(String url,String jsdata)
    {
        String result=null;
        try {
            URL Url = new URL(url);
            HttpURLConnection connection= (HttpURLConnection) Url.openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Charset", "UTF-8");
            connection.setRequestProperty("contentType", "application/json");
            byte[] data = jsdata.getBytes();
            connection.setRequestProperty("Content-Length", String.valueOf(data.length));
            connection.connect();
            OutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(data);
            out.flush();
            out.close();

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
    }  //end of Chengjing driving, Siyu is driving now.


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
    }  //end of Siyu driving, Chengjing is driving now.

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
}  //end of Chengjing driving

