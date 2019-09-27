/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest.resources;
import java.net.Socket;
/**
 *
 * @author juan-
 */
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;


public class ClientSocket extends Thread{
        Socket clientSocket;

        BufferedReader reader;
        PrintWriter writer;
        boolean isEnabled=true;
        private TCPServiceManagerCallerInterface caller;
        private String serverIpAddress;
        private int port;
        
        
        public ClientSocket(Socket clientSocket,
                TCPServiceManagerCallerInterface caller) {
            this.clientSocket = clientSocket;
            this.caller=caller;            
        }
        
        public ClientSocket(String serverIpAddress,
                int port,
                TCPServiceManagerCallerInterface caller) {
            this.serverIpAddress=serverIpAddress;
            this.port=port;            
            this.caller=caller;            
            this.start();
        }
        
        public boolean initializeSocket(){
            try{
                this.clientSocket=new Socket(serverIpAddress,port);
                return true;
            }catch (Exception ex) {
                
            }
            return false;
        }
        
        public boolean initializeStreams(){
            try{
                if(clientSocket==null){
                    if(!initializeSocket()){
                        return false;
                    }
                }
                reader=new BufferedReader(
                        new InputStreamReader(clientSocket.getInputStream()));
                writer=new PrintWriter(
                        new OutputStreamWriter(clientSocket.getOutputStream()),true);
                return true;
            }catch (Exception ex) {
                caller.ErrorHasBeenThrown(ex);
            }
            return false;
        }
        
        
        public void SendMessage(String message){
            try{
                if(clientSocket.isConnected()){
                    writer.write(message+"\n");
                    writer.flush();
                }
            }catch (Exception ex) {
                caller.ErrorHasBeenThrown(ex);
            }
        }
        
        
    }