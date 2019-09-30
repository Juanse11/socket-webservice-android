/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest;

import com.google.gson.Gson;
import com.mycompany.webservicerest.resources.PersistenceUnitHelper;
import com.mycompany.webservicerest.resources.StartupConfig;
import com.server.objects.ChatMessage;
import com.server.objects.Position;
import com.server.objects.ServerQuery;
import com.server.objects.User;
import java.util.List;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * REST Web Service
 *
 * @author asaad
 */
@Path("chat")
@RequestScoped
public class ChatMessageResource {

    EntityManager em;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServerResource
     */
    public ChatMessageResource() {
    }

    /**
     * Retrieves representation of an instance of
     * com.mycompany.webservicerest.ServerResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(String request) throws Exception {
        //TODO return proper representation object
        System.out.println("Request: " + request);
        em = PersistenceUnitHelper.PERSISTENCE_UNIT_2.createEntityManager();
        String sql = "SELECT c FROM ChatMessage c";

        Query query = em.createQuery(sql);
        List<ChatMessage> messages = query.getResultList();
        em.close();
//        StartupConfig.clientSocket.SendMessage("Hola gente de youtube");
        Gson gson = new Gson();
        String s = gson.toJson(messages);
        return s;

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String body) {
        Gson gson = new Gson();
        ChatMessage newMessage = (ChatMessage) gson.fromJson(body, ChatMessage.class);
        em = PersistenceUnitHelper.PERSISTENCE_UNIT_2.createEntityManager();
        em.getTransaction().begin();
        em.persist(newMessage);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return body;
    }

    private String executeQueryAtDatabase(String query) {
        return "{['a','b']}";
    }
}
