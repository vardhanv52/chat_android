package com.android.chat.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class WebServiceAccess {
    private var apiURL = "http://15.207.143.29:3000/"
    // private var apiURL = "http://192.168.43.101:3000/"
    private var httpClient: OkHttpClient.Builder? = null

    init {
        httpClient = OkHttpClient.Builder()
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        httpClient?.addInterceptor(logging)
        httpClient?.connectTimeout(60, TimeUnit.SECONDS)
        httpClient?.addInterceptor(NetworkInterceptor())
    }

    fun getApi(): API {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(apiURL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient!!.build())
            .build()
        return retrofit.create(API::class.java)
    }
}