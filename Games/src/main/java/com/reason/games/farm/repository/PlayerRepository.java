package com.reason.games.farm.repository;

import com.reason.games.farm.model.Player;
import com.reason.games.farm.specification.Specification;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

public class PlayerRepository implements Repository<Player> {

    private final static PlayerRepository instance = new PlayerRepository();
    private final EntityManagerFactory emf;
    private final EntityManager manager;
    private EntityTransaction transaction;

    public PlayerRepository() {
        emf = Persistence.createEntityManagerFactory("FARMPU");
        manager = emf.createEntityManager();
    }

    public static PlayerRepository getInstance() {
        return instance;
    }

    @Override
    public boolean add(Player t) {
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
    public Player findById(int id) {
        if (manager.find(Player.class, id) != null) {
            return manager.find(Player.class, id);
        }
        return null;
    }

    @Override
    public boolean update(Player t) {
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
    public boolean delete(Player t) {
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

    public List<Player> getAll() {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(Player.class);
        Root<Player> taskRoot = query.from(Player.class);
        query.select(taskRoot);
        transaction = manager.getTransaction();
        transaction.begin();
        List<Player> players = manager.createQuery(query).getResultList();
        transaction.commit();
        return players;
    }

    public Player findByName(String name) {
        for (Player player : getAll()) {
            if (player.getName().equalsIgnoreCase(name)) {
                return player;
            }
        }
        return null;
    }

    @Override
    public List<Player> getQuery(Specification<Player> specification) {
        CriteriaBuilder criteriaBuilder = manager.getCriteriaBuilder();
        CriteriaQuery query = criteriaBuilder.createQuery(Player.class);
        Root<Player> taskRoot = query.from(Player.class);
        query.where(specification.toPredicate(taskRoot, criteriaBuilder));
        transaction = manager.getTransaction();
        transaction.begin();
        List<Player> players = manager.createQuery(query).getResultList();
        transaction.commit();
        return players;
    }

}
