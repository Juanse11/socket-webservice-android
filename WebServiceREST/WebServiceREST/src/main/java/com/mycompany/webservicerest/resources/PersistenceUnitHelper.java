/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest.resources;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author juan-
 */
public enum PersistenceUnitHelper {

    PERSISTENCE_UNIT_2("android_pu");

    private final EntityManagerFactory emf;

     
    PersistenceUnitHelper(final String persistenceUnitName) {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
    }

    /**
     * Creates a new {@link EntityManager}. NEVER share this across threads.
     *
     * @return a new {@link EntityManager}
     */
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
}
