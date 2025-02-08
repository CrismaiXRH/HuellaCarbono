package org.example.dao;

import org.example.entities.Actividad;
import org.example.session.Connection;
import org.hibernate.Session;

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
}
