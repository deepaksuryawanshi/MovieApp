package com.Util

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.google.gson.GsonBuilder
import com.google.gson.Gson
import okhttp3.logging.HttpLoggingInterceptor

class ApiClient {

    companion object {

        private const val baseUrl: String = "https://api.themoviedb.org/3/movie/"
        private const val baseUrlDetail: String = "https://api.themoviedb.org/3/"

        fun create(): ApiInterface {

            val interceptor1 = HttpLoggingInterceptor()
            interceptor1.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(headersInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .readTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor1)
//                    .addInterceptor(timeoutInterceptor())
//                    .addInterceptor(connectionTimeOutInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }


        fun createSearch(): ApiInterface {

            val interceptor1 = HttpLoggingInterceptor()
            interceptor1.level = HttpLoggingInterceptor.Level.BODY

            val okHttpClient = OkHttpClient.Builder()
                    .addInterceptor(headersInterceptor())
                    .connectTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .readTimeout(30, TimeUnit.SECONDS) // For testing purposes
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(interceptor1)
//                    .addInterceptor(timeoutInterceptor())
//                    .addInterceptor(connectionTimeOutInterceptor)
                    .build()

            val retrofit = Retrofit.Builder()
                    .baseUrl(baseUrlDetail)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            return retrofit.create(ApiInterface::class.java)
        }


        fun headersInterceptor() = Interceptor { chain ->
            chain.proceed(chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Accept-Language", "en")
                    .addHeader("Content-Type", "application/json")
                    .build())
        }

        fun timeoutInterceptor() = Interceptor { chain ->
//            val request = chain.request()
//            chain.withConnectTimeout(60, TimeUnit.MILLISECONDS)
//                    .withReadTimeout(60, TimeUnit.MILLISECONDS)
//                    .withWriteTimeout(60, TimeUnit.MILLISECONDS)
//                    .proceed(request)

            chain.withConnectTimeout(60, TimeUnit.MILLISECONDS)
                    .withReadTimeout(60, TimeUnit.MILLISECONDS)
                    .withWriteTimeout(60, TimeUnit.MILLISECONDS).proceed(chain.request())
        }

    }

}