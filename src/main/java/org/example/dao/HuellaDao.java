package org.example.dao;

import org.example.entities.Huella;
import org.example.session.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;

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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return huellas;
    }

    public List<Huella> listarHuellasPorUsuario(int userId) {
        Session session = Connection.getInstance().getSession();
        List<Huella> huellas = null;
        try {
            huellas = session.createQuery("SELECT h FROM Huella h WHERE h.usuario.id = :userId", Huella.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return huellas;
    }
}