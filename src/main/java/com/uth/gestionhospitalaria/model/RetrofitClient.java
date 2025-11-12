package com.uth.gestionhospitalaria.model;

import com.uth.gestionhospitalaria.model.Clients.*;
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

    public static HistorialClient getHistorialClient() {
        return getClient().create(HistorialClient.class);
    }

    public static PrescripcionClient getPrescripcionClient() {
        return getClient().create(PrescripcionClient.class);
    }

    public static FacturaClient getFacturaClient() {
        return getClient().create(FacturaClient.class);
    }

}
