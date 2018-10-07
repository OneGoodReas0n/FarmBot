package com.reason.games.farm.repository;

import com.reason.games.farm.specification.Specification;
import java.util.List;

public interface Repository<T> {
    
    public boolean add(T t);
    public T findById(int id);
    public boolean update(T t);
    public boolean delete(T t);
    public List<T> getQuery (Specification<T> specification);
}
