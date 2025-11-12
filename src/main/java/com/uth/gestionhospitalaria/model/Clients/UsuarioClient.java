package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.LoginRequest;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import com.uth.gestionhospitalaria.data.Usuario;
import retrofit2.Call;
import retrofit2.http.*;

public interface UsuarioClient {
    // GET .../GH/usuarios/
    @GET("usuarios/")
    Call<OrdsResponse<Usuario>> listarTodos();

    // POST .../GH/login/
    @POST("login/")
    Call<Usuario> autenticar(@Body LoginRequest loginRequest);

    // GET .../GH/usuarios/{id}
    @GET("usuarios/{id}")
    Call<OrdsResponse<Usuario>> buscarPorId(@Path("id") int id);

    // POST .../GH/usuarios/
    // Esta es la única vez que enviamos la contraseña (para crear)
    @POST("usuarios/")
    Call<Void> crear(@Body Usuario usuario);

    // PUT .../GH/usuarios/{id}
    // Generalmente, no actualizas la contraseña con el resto de los datos.
    // El @Body Usuario NO debería tener la contraseña.
    @PUT("usuarios/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body Usuario usuario);

    // DELETE .../GH/usuarios/{id}
    @DELETE("usuarios/{id}")
    Call<Void> eliminar(@Path("id") int id);
}
