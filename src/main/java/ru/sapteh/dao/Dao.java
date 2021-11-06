package ru.sapteh.dao;

import java.util.List;

public interface Dao <Entity,Key>{
    Entity findById(Key key);
    List<Entity>findAll();
    boolean save(Entity entity);
    boolean update(Entity entity);
    boolean delete(Entity entity);
}
