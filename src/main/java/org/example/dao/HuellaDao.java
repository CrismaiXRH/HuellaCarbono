package org.example.dao;

import org.example.entities.Huella;
import org.example.session.Connection;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.time.LocalDate;
import java.util.List;

public class HuellaDao {

    public void guardarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.save(huella);
            transaction.commit();
            System.out.println("✅ Huella guardada en la base de datos.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("❌ Error al guardar huella: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void actualizarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.update(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void borrarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public Huella listarPorId(int id) {
        Session session = Connection.getInstance().getSession();
        Huella huella = null;
        try {
            huella = session.get(Huella.class, id);
            if (huella != null) {
                Hibernate.initialize(huella.getIdActividad().getIdCategoria());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return huella;
    }

    public List<Huella> listarTodas() {
        Session session = Connection.getInstance().getSession();
        List<Huella> huellas = null;
        try {
            huellas = session.createQuery("from Huella", Huella.class).list();
            for (Huella huella : huellas) {
                Hibernate.initialize(huella.getIdActividad().getIdCategoria());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return huellas;
    }

    public List<Huella> obtenerHuellasDelUsuarioConActividad(int usuarioId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();

        String hql = "FROM Huella h JOIN FETCH h.idActividad a JOIN FETCH a.idCategoria WHERE h.idUsuario.id = :usuarioId";
        Query<Huella> query = session.createQuery(hql, Huella.class);
        query.setParameter("usuarioId", usuarioId);
        List<Huella> huellas = query.list();

        session.getTransaction().commit();
        session.close();
        return huellas;
    }


    public List<Huella> obtenerHuellasPorPeriodo(LocalDate inicio, LocalDate fin) {
        Session session = Connection.getInstance().getSession();
        session.beginTransaction();

        String hql = "FROM Huella h JOIN FETCH h.idActividad a JOIN FETCH a.idCategoria WHERE h.fecha BETWEEN :inicio AND :fin";
        Query<Huella> query = session.createQuery(hql, Huella.class);
        query.setParameter("inicio", inicio);
        query.setParameter("fin", fin);
        List<Huella> huellas = query.list();

        session.getTransaction().commit();
        session.close();
        return huellas;
    }

    public void eliminarHuella(Huella huella) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(huella);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }
}