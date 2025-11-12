package com.uth.gestionhospitalaria.model.Clients;

import com.uth.gestionhospitalaria.data.Factura;
import com.uth.gestionhospitalaria.data.OrdsResponse;
import retrofit2.Call;
import retrofit2.http.*;

public interface FacturaClient {
    @GET("facturas/")
    Call<OrdsResponse<Factura>> listarTodos();

    @GET("facturas/{id}")
    Call<OrdsResponse<Factura>> buscarPorId(@Path("id") int id);

    @POST("facturas/")
    Call<Void> crear(@Body Factura factura);

    @PUT("facturas/{id}")
    Call<Void> actualizar(@Path("id") int id, @Body Factura factura);

    // (DELETE para facturas no es común, pero lo ponemos por el patrón)
    @DELETE("facturas/{id}")
    Call<Void> eliminar(@Path("id") int id);
}