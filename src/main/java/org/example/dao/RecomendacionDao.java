package org.example.dao;

import org.example.entities.Categoria;
import org.example.entities.Recomendacion;
import org.example.session.Connection;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class RecomendacionDao {

    public List<Recomendacion> listarRecomendacionesPorCategorias(List<Categoria> categorias) {
        List<Recomendacion> recomendaciones = null;
        Session session = Connection.getInstance().getSession();
        Transaction transaction = null;

        try {
            transaction = session.beginTransaction();

            // Consulta usando IN para filtrar por categorías
            Query<Recomendacion> query = session.createQuery(
                    "SELECT r FROM Recomendacion r JOIN FETCH r.idCategoria c WHERE c IN :categorias",
                    Recomendacion.class
            );
            query.setParameter("categorias", categorias);
            recomendaciones = query.getResultList();

            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return recomendaciones;
    }
}
