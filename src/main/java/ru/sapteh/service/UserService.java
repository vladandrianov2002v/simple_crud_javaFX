package ru.sapteh.service;

import org.hibernate.SessionFactory;
import ru.sapteh.dao.Dao;
import ru.sapteh.dao.impl.UserDaoImpl;
import ru.sapteh.model.User;

import java.util.List;

public class UserService {
    private final Dao<User, Integer> userDao;

    public UserService(SessionFactory factory) {
        this.userDao = new UserDaoImpl(factory);
    }

    public User findById(int id) {
        return userDao.findById(id);
    }

    public List<User> findAll() {
        return userDao.findAll();
    }

    public boolean save(User user) {
        return userDao.save(user);
    }

    public boolean update(User user) {
        return userDao.save(user);
    }

    public boolean delete(User user) {
       return userDao.save(user);
    }
}