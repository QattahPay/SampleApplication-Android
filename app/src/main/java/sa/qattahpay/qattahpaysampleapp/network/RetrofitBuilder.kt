package sa.qattahpay.qattahpaysampleapp.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import sa.qattahpay.qattahpaysampleapp.BuildConfig
import sa.qattahpay.qattahpaysampleapp.utils.Constants

object RetrofitBuilder {

    private val debugLevel =
        if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY
        else HttpLoggingInterceptor.Level.NONE

    private val interceptor = HttpLoggingInterceptor().setLevel(debugLevel)
    private val okHttpClient = OkHttpClient.Builder().addInterceptor(interceptor).build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl(Constants.apiBaseUrl())
        .addConverterFactory(
            MoshiConverterFactory.create(
                Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
            )
        )
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val retrofitService: RetrofitService by lazy {
        retrofit.create(RetrofitService::class.java)
    }
}