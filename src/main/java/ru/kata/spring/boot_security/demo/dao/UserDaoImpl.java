package ru.kata.spring.boot_security.demo.dao;

import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;
import java.util.List;


@Transactional
@Repository
public class UserDaoImpl implements UserDao {

    @PersistenceContext
    private final EntityManager entityManager;

    public UserDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public List<User> getAllUsers() {
        return entityManager.createQuery("select u from User u", User.class).getResultList();
    }

    @Override
    public Object save(User user) {
        entityManager.persist(user);
        return null;
    }

    @Override
    public User show(int id) {
        TypedQuery<User> query = entityManager.createQuery(
                "select u from User u where u.id = :id", User.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void update(int id, User updateUser) {
        String [] roleUser = new String[] {"ROLE_USER"};

        User user = show(id);
        user.setUsername(updateUser.getUsername());
        user.setSurname(updateUser.getSurname());
        user.setAge(updateUser.getAge());
        user.setEmail(updateUser.getEmail());
        user.setPassword(updateUser.getPassword());
        if (updateUser.getRoles()==null){
            updateUser.setRoles(roleUser);
        }
        user.setRole(updateUser.getRoles());
        entityManager.merge(user);
    }

    @Override
    public void delete(int id) {
        User user = show(id);
        entityManager.remove(user);
    }

    @Override
    public User isExistById(User user) {
        if(entityManager.contains(user)) {
            entityManager.remove(user);
        } else {
            entityManager.remove(entityManager.merge(user));
        }
        return user;
    }
    public User findByUsername(String username){
        return entityManager.createQuery("select u from User u where u.username = :username", User.class)
                .setParameter("username", username).getSingleResult();
    }
    public User findByEmail(String email){
        return entityManager.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email).getSingleResult();
    }
}
