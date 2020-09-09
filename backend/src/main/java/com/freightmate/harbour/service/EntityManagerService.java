package com.freightmate.harbour.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

@Service
public class EntityManagerService {
    EntityManagerFactory emf;

    EntityManagerService(@Autowired EntityManagerFactory emf){
        this.emf = emf;
    }

    public EntityManager getEntityManager() {
        return this.emf.createEntityManager();
    }

}
