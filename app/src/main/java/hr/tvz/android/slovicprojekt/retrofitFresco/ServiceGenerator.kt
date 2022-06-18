package hr.tvz.android.slovicprojekt.retrofitFresco

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ServiceGenerator {
    public fun <S> createService(serviceClass: Class<S>?, baseUrl: String?): S {
        val logging = HttpLoggingInterceptor()
        logging.level = HttpLoggingInterceptor.Level.BODY
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient.build())
            .build()
        return retrofit.create(serviceClass)
    }

    fun <S> createService(serviceClass: Class<S>?, baseUrl: String?, token: AccessToken?): S? {
        if (token != null) {
            val httpClient = OkHttpClient.Builder()
            val retrofitBuilder = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
            httpClient.addInterceptor { chain ->
                val original = chain.request()
                val requestBuilder = original.newBuilder()
                    .header("Accept", "application/json")
                    .header(
                        "Authorization",
                        token.tokenType + " " + token.accessToken
                    )
                    .method(original.method(), original.body())
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            val client = httpClient.build()
            val retrofit = retrofitBuilder.client(client).build()
            return retrofit.create(serviceClass)
        }
        return null
    }
}