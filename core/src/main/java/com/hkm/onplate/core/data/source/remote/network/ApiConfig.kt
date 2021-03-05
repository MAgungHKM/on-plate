package com.hkm.onplate.core.data.source.remote.network

import okhttp3.CertificatePinner
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiConfig {
    companion object {
        fun provideApiService(): ApiService {
            val hostname = "api.spoonacular.com"
            val certificatePinner = CertificatePinner.Builder()
                .add(hostname, "sha256/DjBNmOWrmE91DLaH6gk+96MMBNjNs4/cbGxgvWLZi18=")
                .add(hostname, "sha256/FEzVOUp4dF3gI0ZVPRJhFbSJVXR+uQmMH65xhs1glH4=")
                .add(hostname, "sha256/Y9mvm0exBk1JoQ57f9Vm28jKo5lFm/woKcVxrYxu80o=")
                .add(hostname, "sha256/25Si5gmOzmVMiHQ9F+Ja6xBMK7u761/F87x9Ky/YsPY=")
                .add(hostname, "sha256/hS5jJ4P+iQUErBkvoWBQOd1T7VOAYlOVegvv1iMzpxA=")
                .build()
            val loggingInterceptor =
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .certificatePinner(certificatePinner)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.spoonacular.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}