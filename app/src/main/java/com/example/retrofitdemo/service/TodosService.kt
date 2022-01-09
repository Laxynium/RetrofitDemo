package com.example.retrofitdemo.service

import com.example.retrofitdemo.ui.home.Todo
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

data class CreateTodo(val text: String, val completed: Boolean)
data class UpdateTodo(val text: String, val completed: Boolean)

interface TodosService {

    @GET("api/todos")
    fun getAll(@Query("limit") limit: Int, @Query("offset") offset: Int): Call<List<Todo>>

    @GET("api/todos/{id}")
    fun getById(@Path("id") id: String): Call<Todo>

    @POST("api/todos")
    fun create(@Body todo: CreateTodo): Call<Todo>

    @DELETE("api/todos/{id}")
    fun deleteById(@Path("id") id: Int): Call<Response<Void>>

    @PUT("api/todos/{id}")
    fun updateById(@Path("id") id: Int, @Body todo: UpdateTodo): Call<Todo>

}
