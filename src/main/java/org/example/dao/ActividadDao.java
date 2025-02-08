// File: src/main/java/org/example/dao/ActividadDao.java
package org.example.dao;

import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.session.Connection;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class ActividadDao {

    public List<Actividad> obtenerTodasLasActividades() {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();

        List<Actividad> actividades = session.createQuery("FROM Actividad", Actividad.class).list();

        session.getTransaction().commit();
        session.close();

        return actividades;
    }

    public Actividad obtenerActividad(String nombre) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();

        Actividad actividad = (Actividad) session.createQuery("FROM Actividad WHERE nombre = :nombre")
                .setParameter("nombre", nombre)
                .uniqueResult();

        session.getTransaction().commit();
        session.close();
        return actividad;
    }

    public Actividad obtenerActividadConCategoria(String nombreActividad) {
        Session session = Connection.getInstance().getSession();
        try {
            Actividad actividad = session.createQuery("FROM Actividad WHERE nombre = :nombre", Actividad.class)
                    .setParameter("nombre", nombreActividad)
                    .uniqueResult();
            if (actividad != null) {
                Hibernate.initialize(actividad.getIdCategoria());
            }
            return actividad;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            session.close();
        }
    }

    public List<Huella> obtenerHuellasDelUsuarioConActividad(int usuarioId) {
        Connection connection = Connection.getInstance();
        Session session = connection.getSession();
        session.beginTransaction();

        String hql = "FROM Huella h JOIN FETCH h.idActividad WHERE h.idUsuario.id = :usuarioId";
        Query<Huella> query = session.createQuery(hql, Huella.class);
        query.setParameter("usuarioId", usuarioId);
        List<Huella> huellas = query.list();

        session.getTransaction().commit();
        session.close();
        return huellas;
    }
}