/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest;

import com.google.gson.Gson;
import com.mycompany.webservicerest.resources.PersistenceUnitHelper;
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
@Path("server")
@RequestScoped
public class ServerResource {

    EntityManager em;
    @Context
    private UriInfo context;

    /**
     * Creates a new instance of ServerResource
     */
    public ServerResource() {
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
        
        return "No content";

    }

    /**
     * PUT method for updating or creating an instance of ServerResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {

    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createUser(String user) {
        Gson gson = new Gson();
        System.out.println(gson.fromJson(user, User.class));
        User userob = (User) gson.fromJson(user, User.class);
        User newUser = new User();
        newUser.setFirstname("ronaldo");
        newUser.setLastname("lol");
        em = PersistenceUnitHelper.PERSISTENCE_UNIT_2.createEntityManager();
        em.getTransaction().begin();
        em.persist(newUser);
        em.flush();
        em.getTransaction().commit();
        em.close();

        return Response.ok(user).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public String executeAtServerSide(String serverQuery) {
        String response = "";
        try {
            Gson gson = new Gson();
            ServerQuery inputObject = (ServerQuery) gson.fromJson(serverQuery, ServerQuery.class);
            String databaseResponse = executeQueryAtDatabase(inputObject.getQuery());
            inputObject.setResponse(databaseResponse);
            response = gson.toJson(inputObject);

        } catch (Exception ex) {

        }
        return response;
    }

    private String executeQueryAtDatabase(String query) {
        return "{['a','b']}";
    }
}
