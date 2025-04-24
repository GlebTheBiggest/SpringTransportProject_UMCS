package org.example.app.dao;

import org.example.models.Rental;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import java.util.List;

public class RentalDao {
    private final SessionFactory sessionFactory;

    public RentalDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void save(Rental rental) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(rental);
            tx.commit();
        }
    }

    public Rental findById(String id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rental.class, id);
        }
    }

    public List<Rental> findAll() {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("from Rental", Rental.class).list();
        }
    }

    public void update(Rental rental) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.update(rental);
            tx.commit();
        }
    }

    public void delete(Rental rental) {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(rental);
            tx.commit();
        }
    }

    public void deleteAllRentals() {
        try (Session session = sessionFactory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.createQuery("delete from Rental").executeUpdate();
            tx.commit();
        }
    }
}

