package com.example.retrofitdemo

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetroInstance {
    companion object {
        val BASE_URL = "http://10.0.2.2:8080/"

        fun getRetroInstance(): Retrofit {

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}
