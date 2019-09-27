
  
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest.resources;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author brian
 */
@Singleton
@Startup
public class StartupConfig implements TCPServiceManagerCallerInterface {
    public static ClientSocket clientSocket;
    
    @PostConstruct
    public void onStartup() {
        try {
            clientSocket = new ClientSocket("10.20.38.27",9090,this);
            clientSocket.initializeSocket();
            clientSocket.initializeStreams();
        } catch (Exception e) {
            System.err.println(
                "Initializing web service error: " + e.getMessage()
            );
        }
    }
    
    private void initializeClientSocket() throws FileNotFoundException, IOException {
        
    }   

    @Override
    public void MessageReceiveFromClient(Socket clientSocket, byte[] data) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void ErrorHasBeenThrown(Exception error) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
   
}
