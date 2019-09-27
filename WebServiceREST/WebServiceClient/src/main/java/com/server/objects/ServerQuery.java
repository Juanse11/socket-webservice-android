/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server.objects;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author asaad
 */
public class ServerQuery {
    
    private String userState;
    private String query;
    private String response;

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }
    

    public String getUserState() {
        return userState;
    }

    public void setUserState(String userState) {
        this.userState = userState;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public ServerQuery(String userState, String query) {
        this.userState = userState;
        this.query = query;
    }
    
}
