package com.example.retrofitdemo

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object {
        val BASE_URL = "https://retrofitdemoapi.azurewebsites.net/"

        fun getRetroInstance(): Retrofit {

            val client = OkHttpClient.Builder()
                .addInterceptor(ExampleInterceptor())
//                .addNetworkInterceptor(ExampleInterceptor())
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addCallAdapterFactory(CustomAdapterFactory())
                .build()
        }
    }
}
