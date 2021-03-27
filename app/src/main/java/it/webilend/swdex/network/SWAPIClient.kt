package com.vishal.weather.kotlin.network

import it.webilend.swdex.DEFAULT_CONNECT_TIMEOUT_IN_MS
import it.webilend.swdex.DEFAULT_READ_TIMEOUT_IN_MS
import it.webilend.swdex.DEFAULT_WRITE_TIMEOUT_IN_MS
import it.webilend.swdex.ENDPOINT
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object SWAPIClient {
    lateinit var retrofit: Retrofit

    /**
     * Binds and returns the retorfit client object. We declares global headers, logger and other
     * required interceptors.
     *
     * @return wrapped configuration object of type {@link Retrofit}
     */
    fun getClient(): Retrofit {
        var logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY

        val oktHttpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .connectTimeout(DEFAULT_CONNECT_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(DEFAULT_WRITE_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS)
            .readTimeout(DEFAULT_READ_TIMEOUT_IN_MS, TimeUnit.MILLISECONDS);
        oktHttpClient.addInterceptor(logging)
        oktHttpClient.addInterceptor { chain ->
            var original: Request = chain.request()
            var request: Request = original.newBuilder()
                .method(original.method, original.body)
                .build()
            chain.proceed(request)
        }
        if (!::retrofit.isInitialized) {
            retrofit = Retrofit.Builder()
                .baseUrl(ENDPOINT)
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(oktHttpClient.build())
                .build()
        }
        return retrofit
    }
}