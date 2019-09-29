/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest;

import com.google.gson.Gson;
import com.mycompany.webservicerest.resources.PersistenceUnitHelper;
import com.mycompany.webservicerest.resources.StartupConfig;
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
@Path("users")
@RequestScoped
public class UserResource {

    EntityManager em;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServerResource
     */
    public UserResource() {
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
        String sql = "SELECT u FROM User u";

        Query query = em.createQuery(sql);
        List<User> cars = query.getResultList();
        em.close();
//        StartupConfig.clientSocket.SendMessage("Hola gente de youtube");
        Gson gson = new Gson();
        String s = gson.toJson(cars);
        return s;

    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String putJson(String body) {
        Gson gson = new Gson();
        User newUser = (User) gson.fromJson(body, User.class);

        em = PersistenceUnitHelper.PERSISTENCE_UNIT_2.createEntityManager();
        em.getTransaction().begin();
        User u = em.find(User.class, newUser.getId());
        if (newUser.getStatus() != null) {
            u.setStatus(newUser.getStatus());
        }
        if (newUser.getUsername() != null) {
            u.setUsername(newUser.getUsername());
        }
 
        em.merge(u);
        em.flush();
        em.getTransaction().commit();
        em.close();
        
        return body;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public String createUser(String body) {
        Gson gson = new Gson();
        User newUser = (User) gson.fromJson(body, User.class);
        em = PersistenceUnitHelper.PERSISTENCE_UNIT_2.createEntityManager();
        em.getTransaction().begin();
        em.persist(newUser);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return body;
    }

    private String executeQueryAtDatabase(String query) {
        return "{['a','b']}";
    }
}
