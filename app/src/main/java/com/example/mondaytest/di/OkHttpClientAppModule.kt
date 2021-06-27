package com.example.mondaytest.di


import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object OkHttpClientAppModule {

    private const val TIMEOUT = 10L

    @Provides
    @Singleton
    fun provideGlobalOkHttpClient(): OkHttpClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        return OkHttpClient.Builder()
            .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            .readTimeout(TIMEOUT, TimeUnit.SECONDS)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }
}

/**
 * creates the desired WebService, using retrofit.
 * A global function which can be referenced from all different hilt modules.
 *
 * --------------------------
 * --- *Note* not in di fun !!
 * --------------------------
 */
inline fun <reified T> createWebService(
    okHttpClient: OkHttpClient,
    url: String,
    converterFactory: Converter.Factory = SimpleXmlConverterFactory.create()
): T {

    val retrofit = Retrofit.Builder()
        .baseUrl(url)
        .client(okHttpClient)
        .addConverterFactory(SimpleXmlConverterFactory.create())
        .build()
    return retrofit.create(T::class.java)
}