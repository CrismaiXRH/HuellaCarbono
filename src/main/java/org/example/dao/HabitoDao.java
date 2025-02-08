package org.example.dao;

import org.example.entities.Habito;
import org.example.entities.HabitoId;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.example.session.Connection;

import java.util.List;

public class HabitoDao {

    public void guardarHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            // 🔥 Verificar valores antes de guardar
            System.out.println("Intentando guardar hábito con:");
            System.out.println("Usuario ID: " + (habito.getIdUsuario() != null ? habito.getIdUsuario().getId() : "NULL"));
            System.out.println("Actividad ID: " + (habito.getIdActividad() != null ? habito.getIdActividad().getIdActividad() : "NULL"));
            System.out.println("Frecuencia: " + habito.getFrecuencia());
            System.out.println("Tipo: " + habito.getTipo());
            System.out.println("Última Fecha: " + habito.getUltimaFecha());

            session.save(habito);
            transaction.commit();
            System.out.println("✅ Hábito guardado en la base de datos.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            System.out.println("❌ Error al guardar hábito: " + e.getMessage());
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    public void actualizarHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.update(habito);
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

    public void borrarHabito(Habito habito) {
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();
            session.delete(habito);
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

    public Habito listarPorId(HabitoId id) {
        Session session = Connection.getInstance().getSession();
        Habito habito = null;

        try {
            habito = session.get(Habito.class, id);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return habito;
    }

    public List<Habito> ListarTodos() {
        Session session = Connection.getInstance().getSession();
        List<Habito> habitos = null;

        try {
            habitos = session.createQuery("from Habito", Habito.class).list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return habitos;
    }

    public List<Habito> listarHabitosPorUsuario(int userId) {
        Session session = Connection.getInstance().getSession();
        List<Habito> habitos = null;

        try {
            // Usamos HQL para filtrar por usuario y hacer join fetch de actividad
            habitos = session.createQuery("SELECT h FROM Habito h JOIN FETCH h.idActividad WHERE h.idUsuario.id = :userId", Habito.class)
                    .setParameter("userId", userId)
                    .list();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return habitos;
    }
}