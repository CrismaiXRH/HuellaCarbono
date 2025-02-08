package org.example.services;

import org.example.dao.ActividadDao;
import org.example.dao.HabitoDao;
import org.example.entities.Actividad;
import org.example.entities.Habito;
import org.example.entities.Usuario;

import java.time.LocalDate;
import java.util.List;

public class HabitoServices {
    private final HabitoDao habitoDao = new HabitoDao();
    private final ActividadDao actividadDao = new ActividadDao();

    public List<Habito> listarHabitosPorUsuario(int usuarioId) {
        if (usuarioId <= 0) {
            throw new IllegalArgumentException("El ID del usuario debe ser mayor que cero");
        }
        return habitoDao.listarHabitosPorUsuario(usuarioId);
    }

    public void eliminarHabito(Habito habito) {
        if (habito == null) {
            throw new IllegalArgumentException("El hábito no puede estar vacío");
        }
        habitoDao.eliminarHabito(habito);
    }

    public void guardarHabito(Usuario usuario, String actividadSeleccionada, String frecuenciaTexto, String tipo, LocalDate ultimaFecha) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede estar vacío");
        }
        if (actividadSeleccionada == null || actividadSeleccionada.isEmpty()) {
            throw new IllegalArgumentException("La actividad no puede estar vacía");
        }
        if (frecuenciaTexto == null || frecuenciaTexto.isEmpty()) {
            throw new IllegalArgumentException("La frecuencia no puede estar vacía");
        }
        if (tipo == null || tipo.isEmpty()) {
            throw new IllegalArgumentException("El tipo no puede estar vacío");
        }
        if (ultimaFecha == null) {
            throw new IllegalArgumentException("La fecha no puede estar vacía");
        }

        int frecuencia;
        try {
            frecuencia = Integer.parseInt(frecuenciaTexto);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("La frecuencia debe ser un número válido");
        }
        if (frecuencia <= 0) {
            throw new IllegalArgumentException("La frecuencia debe ser un número positivo");
        }

        Actividad actividad = actividadDao.obtenerActividad(actividadSeleccionada);
        if (actividad == null) {
            throw new IllegalArgumentException("Actividad no encontrada");
        }

        Habito nuevoHabito = new Habito(usuario, actividad, frecuencia, tipo, ultimaFecha);
        habitoDao.guardarHabito(nuevoHabito);
    }

    public List<Actividad> obtenerTodasLasActividades() {
        return actividadDao.obtenerTodasLasActividades();
    }
}