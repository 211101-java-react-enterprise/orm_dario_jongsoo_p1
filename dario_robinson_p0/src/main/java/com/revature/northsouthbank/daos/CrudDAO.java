package com.revature.northsouthbank.daos;

import com.revature.northsouthbank.util.collections.List;

public interface CrudDAO<T> {
    T save(T newObj);
    List<T> findAll();
    T findByID(String id);
    boolean update(T updatedObj);
    boolean removeById(String id);
}
