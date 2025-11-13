package com.uth.gestionhospitalaria.model.Implements;

import com.uth.gestionhospitalaria.data.LoginRequest;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Usuario;
import com.uth.gestionhospitalaria.model.Clients.UsuarioClient;
import com.uth.gestionhospitalaria.model.RetrofitClient;
import com.uth.gestionhospitalaria.model.repositories.IUsuarioRepository;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

public class UsuarioRepositoryImpl implements IUsuarioRepository {
    private final UsuarioClient usuarioClient;

    public UsuarioRepositoryImpl(){
        this.usuarioClient = RetrofitClient.getUsuarioApiClient();
    }

    @Override
    public List<Usuario> listarTodos() {
        try {
            Call<OrdsResponse<Usuario>> call = usuarioClient.listarTodos();
            Response<OrdsResponse<Usuario>> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                return response.body().getItems();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    @Override
    public Usuario buscarPorId(int id) {
        try {
            Call<OrdsResponse<Usuario>> call = usuarioClient.buscarPorId(id);
            Response<OrdsResponse<Usuario>> response = call.execute();

            if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                return response.body().getItems().getFirst();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean crear(Usuario usuario) {
        try {
            // Llama al método 'crear' del cliente
            Call<Void> call = usuarioClient.crear(usuario);
            // Ejecuta la llamada
            Response<Void> response = call.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean actualizar(Usuario usuario) {
        try {
            // Regla de negocio: no enviar la contraseña en una actualización de perfil
            usuario.setPassword(null);

            Call<Void> call = usuarioClient.actualizar(usuario.getId_usuario(), usuario);
            Response<Void> response = call.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean eliminar(int id) {
        try {
            Call<Void> call = usuarioClient.eliminar(id);
            Response<Void> response = call.execute();
            return response.isSuccessful();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public Usuario autenticar(String dni, String password) {
        try {
            // 1. Crear el objeto de solicitud con los datos
            LoginRequest requestBody = new LoginRequest(dni, password);

            // 2. Llamar al método 'autenticar' del cliente
            Call<Usuario> call = usuarioClient.autenticar(requestBody);

            Response<Usuario> response = call.execute();

            if (response.isSuccessful() && response.body() != null) {
                //  responde 200 OK, devuelve el objeto Usuario
                return response.body();
            }

            // Si no fue exitosa (ej: 401 Unauthorized),
            // la ejecución continúa y devuelve null.

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Devuelve null si las credenciales son incorrectas o hay un error de red
        return null;
    }
}
