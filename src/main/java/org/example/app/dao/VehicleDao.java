package org.example.app.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;
import org.example.models.Vehicle;

public class VehicleDao {
    private final SessionFactory sessionFactory;

    public VehicleDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(vehicle);
            tx.commit();
        }
    }

    public Vehicle findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Vehicle.class, id);
        }
    }

    public List<Vehicle> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Vehicle", Vehicle.class).list();
        }
    }

    public void update(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(vehicle);
            tx.commit();
        }
    }

    public void delete(Vehicle vehicle) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(vehicle);
            tx.commit();
        }
    }

    public void deleteAll() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Vehicle").executeUpdate();
            tx.commit();
        }
    }
}
