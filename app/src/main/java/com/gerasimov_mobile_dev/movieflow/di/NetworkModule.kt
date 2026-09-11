package com.gerasimov_mobile_dev.movieflow.di

import android.util.Log
import com.gerasimov_mobile_dev.movieflow.data.remote.PoiskKinoApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideMoshi(): Moshi = Moshi.Builder()
        .add(KotlinJsonAdapterFactory())
        .build()

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        return OkHttpClient.Builder()
//            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request()
                // request.method теперь свойство, не метод
                Log.d("CustomOkHttp", "--> ${request.method} ${request.url}")

                val response = chain.proceed(request)

                // response.body теперь свойство; читаем тело один раз
                val rawBody = response.body?.string() ?: "no body"

                Log.d("CustomOkHttp", "<-- ${response.code} ${response.request.url}")
                Log.d("CustomOkHttp", "BODY: $rawBody")

                // Обязательно возвращаем новый Response с тем же телом, иначе поток закроется
                response.newBuilder()
                    .body(
                        okhttp3.ResponseBody.create(
                            response.body?.contentType(),
                            rawBody
                        )
                    )
                    .build()
            }
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient, moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl("https://api.poiskkino.dev/")
            .client(client)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides
    @Singleton
    fun providePoiskKinoApi(retrofit: Retrofit): PoiskKinoApi =
        retrofit.create(PoiskKinoApi::class.java)
}

