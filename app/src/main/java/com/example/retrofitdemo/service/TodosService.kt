package com.example.retrofitdemo.service

import com.example.retrofitdemo.model.Todo
import com.example.retrofitdemo.model.UpdateTodo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface TodosService {

    @GET("/api/todos")
    fun getAll(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<List<Todo>>

    @GET("/api/todos/{id}")
    fun getById(@Path("id") id: String): Call<Todo>

    @POST("/api/todos")
    fun create(@Body todo: Todo): Call<Todo>

    @DELETE("/api/todos/{id}")
    fun deleteById(@Path("id") id: String): Call<Response<Void>>

    @PUT("/api/todos/{id}")
    fun updateById(@Path("id") id: String, @Body todo: UpdateTodo): Call<Response<Void>>

}
