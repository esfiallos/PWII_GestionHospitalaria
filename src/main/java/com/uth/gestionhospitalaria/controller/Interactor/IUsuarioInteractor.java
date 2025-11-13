package com.uth.gestionhospitalaria.controller.Interactor;

import com.uth.gestionhospitalaria.data.Usuario;
import java.util.List;

public interface IUsuarioInteractor {

        Usuario autenticarUsuario(String dni, String password);

        List<Usuario> consultarUsuarios();

        Usuario consultarUsuarioPorId(int id);

        boolean registrarUsuario(Usuario usuario);

        boolean actualizarUsuario(Usuario usuario);

        boolean eliminarUsuario(int id);

}
