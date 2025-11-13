package com.uth.gestionhospitalaria.model.repositories;

import com.uth.gestionhospitalaria.data.Usuario;

import java.util.List;

public interface IUsuarioRepository {

    List<Usuario> listarTodos();

    Usuario buscarPorId(int id);

    boolean crear(Usuario usuario);

    boolean actualizar(Usuario paciente);

    boolean eliminar(int id);

    public Usuario autenticar(String dni, String password);
}
