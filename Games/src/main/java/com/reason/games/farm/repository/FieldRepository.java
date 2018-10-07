package com.reason.games.farm.repository;

import com.reason.games.farm.model.GameField;
import com.reason.games.farm.specification.Specification;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class FieldRepository implements Repository<GameField>{

    private final static FieldRepository instance = new FieldRepository();
    private final EntityManagerFactory emf;
    private final EntityManager manager;
    private EntityTransaction transaction;

    public FieldRepository() {
        this.emf = Persistence.createEntityManagerFactory("FARMPU");
        this.manager = emf.createEntityManager();
    }

    public static FieldRepository getInstance() {
        return instance;
    }
    
    
    
    @Override
    public boolean add(GameField t) {
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.persist(t);
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public GameField findById(int id) {
        return manager.find(GameField.class, id);
    }

    @Override
    public boolean update(GameField t) {
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.merge(t);
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public boolean delete(GameField t) {
        try {
            transaction = manager.getTransaction();
            transaction.begin();
            manager.remove(t);
            transaction.commit();
            return true;
        } catch (Exception e) {
            System.err.println(e);
            return false;
        }
    }

    @Override
    public List<GameField> getQuery(Specification<GameField> specification) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(GameField.class);
        Root<GameField> taskRoot = query.from(GameField.class);
        query.where(specification.toPredicate(taskRoot, criteriaBuilder));
        transaction = manager.getTransaction();
        transaction.begin();
        List<GameField> fields = manager.createQuery(query).getResultList();
        transaction.commit();
        return fields;
    }
    
    
}
