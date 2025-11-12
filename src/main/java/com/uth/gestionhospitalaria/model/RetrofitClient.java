package com.uth.gestionhospitalaria.model;

import com.uth.gestionhospitalaria.model.Clients.CitaClient;
import com.uth.gestionhospitalaria.model.Clients.PacienteClient;
import com.uth.gestionhospitalaria.model.Clients.UsuarioClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://oracleapex.com/ords/uthpw/GH/";

    private static Retrofit retrofit = null;

    // Método privado para construir Retrofit
    private static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    public static PacienteClient getPacienteApiClient() {
        return getClient().create(PacienteClient.class);
    }

    public static UsuarioClient getUsuarioApiClient() {
        return getClient().create(UsuarioClient.class);
    }

    public static CitaClient getCitaApiClient() {
        return getClient().create(CitaClient.class);
    }
}
