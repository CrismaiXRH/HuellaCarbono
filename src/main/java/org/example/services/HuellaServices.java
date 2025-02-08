package org.example.services;

import org.example.dao.ActividadDao;
import org.example.dao.HuellaDao;
import org.example.entities.Actividad;
import org.example.entities.Huella;
import org.example.entities.Usuario;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public class HuellaServices {
    private final ActividadDao actividadDao = new ActividadDao();
    private final HuellaDao huellaDao = new HuellaDao();

    public List<Actividad> obtenerTodasLasActividades() {
        return actividadDao.obtenerTodasLasActividades();
    }

    public Actividad obtenerActividadConCategoria(String nombreActividad) {
        if (nombreActividad == null || nombreActividad.isEmpty()) {
            throw new IllegalArgumentException("El nombre de la actividad no puede estar vacío");
        }
        return actividadDao.obtenerActividadConCategoria(nombreActividad);
    }

    public void guardarHuella(Usuario usuario, String actividadSeleccionada, String cantidadTexto, String unidad, LocalDate fecha) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        if (actividadSeleccionada == null || actividadSeleccionada.isEmpty()) {
            throw new IllegalArgumentException("La actividad no puede estar vacía");
        }
        if (cantidadTexto == null || cantidadTexto.isEmpty()) {
            throw new IllegalArgumentException("La cantidad no puede estar vacía");
        }
        if (unidad == null || unidad.isEmpty()) {
            throw new IllegalArgumentException("La unidad no puede estar vacía");
        }
        if (fecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }
        if (!isValidNumber(cantidadTexto)) {
            throw new IllegalArgumentException("La cantidad debe ser un número válido");
        }

        BigDecimal cantidad = new BigDecimal(cantidadTexto);
        Actividad actividad = obtenerActividadConCategoria(actividadSeleccionada);
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no encontrada");
        }

        Huella nuevaHuella = new Huella(usuario, actividad, cantidad, unidad, fecha);
        huellaDao.guardarHuella(nuevaHuella);
    }

    public List<Huella> obtenerHuellasDelUsuarioConActividad(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor que cero");
        }
        return huellaDao.obtenerHuellasDelUsuarioConActividad(usuarioId);
    }

    public List<Huella> obtenerHuellasPorPeriodo(LocalDate inicio, LocalDate fin) {
        if (inicio == null || fin == null) {
            throw new IllegalArgumentException("Las fechas de inicio y fin no pueden estar vacías");
        }
        return huellaDao.obtenerHuellasPorPeriodo(inicio, fin);
    }

    public void eliminarHuella(Huella huella) {
        if (huella == null) {
            throw new IllegalArgumentException("La huella no puede estar vacía");
        }
        huellaDao.eliminarHuella(huella);
    }

    private boolean isValidNumber(String value) {
        try {
            new BigDecimal(value);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}