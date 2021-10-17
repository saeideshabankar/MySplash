package com.example.mysplash.data.sevises

import androidx.multidex.BuildConfig
import com.example.mysplash.utils.ACCEPT
import com.example.mysplash.utils.APPLICATION_JSON
import com.example.mysplash.utils.BASE_URL
import com.example.mysplash.utils.NETWORK_CONNECTION_TIME
import com.google.gson.GsonBuilder
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ApiClient {
    private val apiServices: ApiServices

    init {
        //Gson you can init Gson
        val gson = GsonBuilder()
            .setLenient()
            .create()

        //Http we set log level for loggingInterceptor this is set in itself when must show log to us,at the end level placed in interceptors(list)
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level =
            if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.NONE

        //Http builder -> 1: here u add top loggingInterceptor in list 2:and set header in request
        val clientBuilder = OkHttpClient.Builder()
        clientBuilder.interceptors().add(loggingInterceptor)
        clientBuilder.addInterceptor { chain ->
            val request = chain.request()
            request.newBuilder().addHeader(ACCEPT, APPLICATION_JSON).build()
            chain.proceed(request)
        }
        //http client
        // readTimeOut is time for when connection wants to read from server and cant
        //writeTimeOut is time for when we wants to write on server and we cant
        //when we cant connect for every reasons
        //try again for connection
        val client = clientBuilder
            .readTimeout(NETWORK_CONNECTION_TIME, TimeUnit.SECONDS)
            .writeTimeout(NETWORK_CONNECTION_TIME, TimeUnit.SECONDS)
            .connectTimeout(NETWORK_CONNECTION_TIME, TimeUnit.SECONDS)
            .retryOnConnectionFailure(true)
            .build()

        //Retrofit
        //retrofit is based on okHttp and here we put our settings and connections to retrofit
        //1 ->url  2 ->put our settings 3->match gson and retrofit to gather 4->rx works on another tread for  retrofit connection
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .build()

        //Initialize url (where page we wants to go on server)
        apiServices = retrofit.create(ApiServices::class.java)
    }

    //companion object is static object
    companion object {
        private var apiClient: ApiClient? = null

        fun getInstance(): ApiClient = apiClient ?: synchronized(this) {
            apiClient ?: ApiClient().also {
                apiClient = it
            }
        }
    }

    fun apiUseCase(): ApiUseCase {
        return ApiUseCase(apiServices)
    }
}