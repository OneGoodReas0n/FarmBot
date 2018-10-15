package com.reason.games.farm.repository;

import com.reason.games.farm.model.Cell;
import com.reason.games.farm.specification.Specification;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class CellRepository implements Repository<Cell> {

    private final static CellRepository instance = new CellRepository();
    private final EntityManagerFactory emf;
    private final EntityManager manager;
    private EntityTransaction transaction;

    public CellRepository() {
        this.emf = Persistence.createEntityManagerFactory("FARMPU");
        this.manager = emf.createEntityManager();
    }

    public static CellRepository getInstance() {
        return instance;
    }

    @Override
    public boolean add(Cell t) {
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
    public Cell findById(int id) {
        return manager.find(Cell.class, id);
    }

    @Override
    public boolean update(Cell t) {
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
    public boolean delete(Cell t) {
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
    public List<Cell> getQuery(Specification<Cell> specification) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(Cell.class);
        Root<Cell> taskRoot = query.from(Cell.class);
        query.where(specification.toPredicate(taskRoot, criteriaBuilder));
        transaction = manager.getTransaction();
        transaction.begin();
        List<Cell> cells = manager.createQuery(query).getResultList();
        transaction.commit();
        return cells;
    }
}
