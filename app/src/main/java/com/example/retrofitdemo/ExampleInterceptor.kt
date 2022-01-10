package com.example.retrofitdemo

import android.util.Log
import okhttp3.Interceptor
import okhttp3.RequestBody
import okhttp3.Response
import okio.Buffer
import java.io.IOException

class ExampleInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request();

        val t1 = System.nanoTime()
        Log.d(
            "HTTPInterceptor",
            "Sending request ${request.method()} ${request.url()}\n${request.headers()}\n${bodyToString(request.body())}"
        )

        val response = chain.proceed(request);

        val t2 = System.nanoTime();
        Log.d(
            "HTTPInterceptor",
            "Received response ${response.code()} for ${
                response.request().url()
            } in ${(t2 - t1) / 1e6}ms\n${response.headers()}\n${response.peekBody(Long.MAX_VALUE).string()}"
        )

        return response.newBuilder().build()
    }

    private fun bodyToString(request: RequestBody?): String {
        try {
            val copy = request;
            val buffer = Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (e: IOException) {
            return "did not work";
        }
    }
}