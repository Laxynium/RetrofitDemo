package com.example.retrofitdemo.service

import com.example.retrofitdemo.model.Todo
import com.example.retrofitdemo.model.UpdateTodo
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TodosService {

    @Multipart
    @POST("/api/Files")
    @Headers("Host: retrofitdemo.azurewebsites.net", "Accept: */*")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>

    @GET("/api/Todos")
    fun getAll(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<List<Todo>>

    @GET("/api/Todos/{id}")
    fun getById(@Path("id") id: String): Call<Todo>

    @POST("/api/Todos")
    fun create(@Body todo: Todo): Call<Todo>

    @DELETE("/api/Todos/{id}")
    fun deleteById(@Path("id") id: String): Call<Response<Void>>

    @PUT("/api/Todos/{id}")
    fun updateById(@Path("id") id: String, @Body todo: UpdateTodo): Call<Response<Void>>

}
