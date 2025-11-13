package com.uth.gestionhospitalaria.controller.Implements;

import com.uth.gestionhospitalaria.controller.Interactor.IUsuarioInteractor;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.model.Implements.UsuarioRepositoryImpl;
import com.uth.gestionhospitalaria.model.repositories.IUsuarioRepository;

import java.util.List;

public class UsuarioInteractorImpl implements IUsuarioInteractor {

    private final IUsuarioRepository usuarioRepository;

    public UsuarioInteractorImpl() {
        this.usuarioRepository = new UsuarioRepositoryImpl();
    }

    @Override
    public Usuario autenticarUsuario(String dni, String password) {

        if (dni == null || dni.trim().isEmpty()) {
            System.err.println("Interactor: El DNI no puede estar vacío.");
            return null;
        }
        if (password == null || password.isEmpty()) {
            System.err.println("Interactor: La contraseña no puede estar vacía.");
            return null;
        }

        return this.usuarioRepository.autenticar(dni, password);
    }

    @Override
    public List<Usuario> consultarUsuarios() {
        return this.usuarioRepository.listarTodos();
    }

    @Override
    public Usuario consultarUsuarioPorId(int id) {
        return this.usuarioRepository.buscarPorId(id);
    }

    @Override
    public boolean registrarUsuario(Usuario usuario) {
        if (usuario.getDni() == null || usuario.getDni().trim().isEmpty() ||
                usuario.getPassword() == null || usuario.getPassword().isEmpty()) {
            System.err.println("Error de negocio: DNI y Password son obligatorios.");
            return false;
        }
        // Aquí iría la lógica para HASHEAR la contraseña antes de enviarla
        // Por ahora, la pasamos tal cual
        return this.usuarioRepository.crear(usuario);
    }

    @Override
    public boolean actualizarUsuario(Usuario usuario) {
        if (usuario.getId_usuario() == 0 || usuario.getDni() == null || usuario.getDni().trim().isEmpty()) {
            System.err.println("Error de negocio: ID y DNI son obligatorios.");
            return false;
        }
        return this.usuarioRepository.actualizar(usuario);
    }

    @Override
    public boolean eliminarUsuario(int id) {
        return this.usuarioRepository.eliminar(id);
    }
}
