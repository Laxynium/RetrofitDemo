package com.example.retrofitdemo

import androidx.lifecycle.LiveData
import arrow.core.Either
import retrofit2.*
import java.io.IOException
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

sealed class ApiError {
    data class HttpError(val code: Int, val body: String) : ApiError()
    data class NetworkError(val throwable: Throwable) : ApiError()
    data class UnknownApiError(val throwable: Throwable) : ApiError()
}

private class CustomCallAdapter<R>(private val responseType: Type) :
    CallAdapter<R, LiveData<Either<ApiError, R>>> {
    override fun responseType(): Type {
        return responseType
    }

    override fun adapt(call: Call<R>): LiveData<Either<ApiError, R>> {
        return object : LiveData<Either<ApiError, R>>() {
            private val started: AtomicBoolean = AtomicBoolean(false)
            override fun onActive() {
                super.onActive()
                if (started.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<R> {
                        override fun onResponse(call: Call<R>, response: Response<R>) {
                            if (!response.isSuccessful) {
                                val errorBody = response.errorBody()?.string() ?: ""
                                postValue(
                                    Either.left(
                                        ApiError.HttpError(
                                            response.code(),
                                            errorBody
                                        )
                                    )
                                )
                                return
                            }

                            response.body()?.let {
                                postValue(Either.right(it))
                                return
                            }
                            if (responseType == Unit::class.java) {
                                @Suppress("UNCHECKED_CAST")
                                Either.Right(Unit) as Either<ApiError, R>
                            } else {
                                @Suppress("UNCHECKED_CAST")
                                Either.Left(UnknownError("Response body was null")) as Either<ApiError, R>
                            }
                        }

                        override fun onFailure(call: Call<R>, t: Throwable) {
                            val error = when (t) {
                                is IOException -> ApiError.NetworkError(t)
                                else -> ApiError.UnknownApiError(t)
                            }
                            postValue(Either.left(error))
                        }

                    })
                }
            }
        }
    }
}

class CustomAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }
        check(returnType is ParameterizedType) { "Return type must be a parameterized type." }

        val responseType = getParameterUpperBound(0, returnType)
        if (getRawType(responseType) != Either::class.java) return null
        check(responseType is ParameterizedType) { "Response type must be a parameterized type." }

        val leftType = getParameterUpperBound(0, responseType)
        if (getRawType(leftType) != ApiError::class.java) return null

        val rightType = getParameterUpperBound(1, responseType)
        return CustomCallAdapter<Any>(rightType)
    }
}