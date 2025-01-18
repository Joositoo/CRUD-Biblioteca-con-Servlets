package org.example.servletbiblioteca.Modelo;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;

import java.util.List;


public class DAO<T, ID> {
    EntityManagerFactory emf = Persistence.createEntityManagerFactory("biblioteca");
    EntityManager em = emf.createEntityManager();
    EntityTransaction tx = em.getTransaction();
    Class<T> clase;
    Class<ID> claseID;

    public DAO(Class<T> clase, Class<ID> claseID){
        this.clase=clase;
        this.claseID=claseID;
    }

    //INSERT
    public void insert(T objeto){
        tx.begin();
        em.persist(objeto);
        tx.commit();
    }

    //SELECT WHERE ID
    public T selectById(ID id){
        return em.find(clase, id);
    }

    //SELECT *
    public List<T> selectAll(){
        return em.createQuery("SELECT c from "+ clase.getName()+" c").getResultList();
    }

    //UPDATE
    public T update(T objeto){
        tx.begin();
        objeto = em.merge(objeto);
        tx.commit();
        return objeto;
    }
    //DELETE
    public void delete(T objeto){
        tx.begin();
        em.remove(objeto);
        tx.commit();
    }

    @Override
    public String toString() {
        return "DAO{" +
                "emf=" + emf +
                ", em=" + em +
                ", tx=" + tx +
                ", clase=" + clase +
                ", claseID=" + claseID +
                '}';
    }
}

