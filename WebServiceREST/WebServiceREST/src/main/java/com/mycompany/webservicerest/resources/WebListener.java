/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.webservicerest.resources;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 *
 * @author juan-
 */

public class WebListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent event) {
        // Do your job here during webapp startup.
    }

    @Override
    public void contextDestroyed(ServletContextEvent event) {
        // Do your job here during webapp shutdown.
        System.out.println("LOOLOLOLOASLDOLASODOAISDJFLASKDJFHLASKDJFHASLKDJFHASLDKJFHASLDKFHASLDKFJHASLDKJFHSLDFJAHSLDFKJASHDLFKJAHSDFLKAJSHDFLAKJSDHFLAKSDJ");
        PersistenceUnitHelper.PERSISTENCE_UNIT_2.closeFactory();
    }

}