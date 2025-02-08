package org.example.services;

import org.example.dao.RecomendacionDao;
import org.example.entities.Categoria;
import org.example.entities.Recomendacion;

import java.util.List;

public class RecomendacionesServices {
    private final RecomendacionDao recomendacionDao = new RecomendacionDao();

    public List<Recomendacion> listarRecomendacionesPorCategorias(List<Categoria> categorias) {
        if (categorias == null || categorias.isEmpty()) {
            throw new IllegalArgumentException("La lista de categorías no puede estar vacía");
        }
        return recomendacionDao.listarRecomendacionesPorCategorias(categorias);
    }
}