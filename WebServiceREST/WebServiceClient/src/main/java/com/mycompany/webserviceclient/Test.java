/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webserviceclient;

import com.google.gson.Gson;
import com.server.objects.ServerQuery;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 *
 * @author asaad
 */
public class Test {
    
    public static void main(String[] args){
        try{
            
            String endpoint="http://localhost:8080/WebServiceREST/resources/server";
            String methodType="PUT";
            String action="executeAtServerSide";
            URL url=new URL(endpoint+"/"+action);
            HttpURLConnection urlConnection=(HttpURLConnection)url.openConnection();
            urlConnection.setRequestMethod(methodType);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            PrintWriter writer=new PrintWriter(urlConnection.getOutputStream());
            ServerQuery query;
            query=new ServerQuery("Login", "select * from users t where t.username='asaad' and t.password ='12345'");
            Gson gson=new Gson();
            String serializedQuery=gson.toJson(query);
            writer.write(serializedQuery);
            writer.flush();
            int httpResponseCode=urlConnection.getResponseCode();
            if(httpResponseCode==HttpURLConnection.HTTP_OK){
                BufferedReader reader=new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String newLine="";
                while((newLine=reader.readLine())!=null){
                    System.out.println(newLine);
                }
            }
            
            
            
        }catch (Exception ex) {
            System.err.println(ex);
        }
    }
    
}
