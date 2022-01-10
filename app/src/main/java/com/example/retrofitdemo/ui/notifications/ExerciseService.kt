package com.example.retrofitdemo.ui.notifications

import io.reactivex.rxjava3.core.Observable
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

data class Code(val code:String, val name: String);
data class ValidationSuccessResponse(val message:String, val code: String)
interface ExerciseService {
    @POST("api/exercise/code/{name}")
    fun generateCode(@Path("name") name: String): Observable<Response<Code>>
    @GET("api/exercise/code/{name}/validate")
    fun validate(@Path("name") name: String, @Header("X-Custom-Code") header:String):Observable<Response<ValidationSuccessResponse>>
}