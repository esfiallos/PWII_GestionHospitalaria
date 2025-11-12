package com.uth.gestionhospitalaria.model; // O tu paquete 'model'

import com.uth.gestionhospitalaria.model.Clients.*;
import okhttp3.Interceptor; // ¡NUEVA IMPORTACIÓN!
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;  // ¡NUEVA IMPORTACIÓN!
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.net.ProxySelector;
import java.net.URI;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "https://oracleapex.com/ords/uthpw/GH/";

    private static Retrofit retrofit = null;

    private static Retrofit getClient() {
        if (retrofit == null) {
            try {
                OkHttpClient.Builder httpClient = new OkHttpClient.Builder();

                // 1. Mantenemos los timeouts
                httpClient.connectTimeout(30, TimeUnit.SECONDS);
                httpClient.readTimeout(30, TimeUnit.SECONDS);
                httpClient.writeTimeout(30, TimeUnit.SECONDS);

                // 2. Mantenemos el fix de HTTP/1.1
                httpClient.protocols(List.of(Protocol.HTTP_1_1));

                // 3. Mantenemos el selector de Proxy
                System.setProperty("java.net.useSystemProxies", "true");
                List<java.net.Proxy> proxies = ProxySelector.getDefault().select(new URI(BASE_URL));
                if (proxies != null && !proxies.isEmpty()) {
                    httpClient.proxy(proxies.get(0));
                }

                // --- 4. NUEVO FIX: Añadir un User-Agent de Navegador ---
                // Esto "disfraza" nuestra solicitud para que parezca un navegador Chrome
                httpClient.addInterceptor(new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws java.io.IOException {
                        Request original = chain.request();
                        Request.Builder requestBuilder = original.newBuilder()
                                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36");

                        Request request = requestBuilder.build();
                        return chain.proceed(request);
                    }
                });
                // --- FIN DEL NUEVO FIX ---

                // 5. Construimos Retrofit
                retrofit = new Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .client(httpClient.build())
                        .build();

            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException("Error al configurar RetrofitClient", e);
            }
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

   /* public static MedicamentoClient getMedicamentoClient() {
        return getClient().create(MedicamentoClient.class);
    }*/

    public static PrescripcionClient getPrescripcionClient() {
        return getClient().create(PrescripcionClient.class);
    }

    public static FacturaClient getFacturaClient() {
        return getClient().create(FacturaClient.class);
    }
}