package com.example.retrofitdemo.service

import androidx.lifecycle.LiveData
import arrow.core.Either
import com.example.retrofitdemo.ApiError
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import com.example.retrofitdemo.ui.home.Todo
import io.reactivex.rxjava3.core.Observable
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

data class CreateTodo(val text: String, val completed: Boolean)
data class UpdateTodo(val text: String, val completed: Boolean)

interface TodosService {

    @Multipart
    @POST("/api/Files")
    @Headers("Host: retrofitdemo.azurewebsites.net", "Accept: */*")
    fun uploadImage(@Part file: MultipartBody.Part): Call<ResponseBody>

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

    @GET("api/todos")
    fun getAllObs(@Query("limit") limit: Int, @Query("offset") offset: Int): Observable<List<Todo>>

    @PUT("api/todos/{id}")
    fun updateByIdObs(@Path("id") id: Int, @Body todo: UpdateTodo): Observable<Response<Todo>>

    @GET("api/todos")
    fun getAllCustom(@Query("limit") limit: Int, @Query("offset") offset: Int): LiveData<Either<ApiError,List<Todo>>>
}
