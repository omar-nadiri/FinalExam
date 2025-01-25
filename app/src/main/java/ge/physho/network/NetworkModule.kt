package ge.physho.network

import android.content.Context
import android.content.SharedPreferences
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import ge.physho.App.Companion.appContext
import ge.physho.network.endpoints.PetFinderEndpoint
import ge.physho.network.endpoints.RefreshEndpoint
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

object NetworkModule {

    private const val BASE_URL = "https://api.petfinder.com/v2/"
    private const val TOKEN_FILE_KEY = "com.chaseolson.pets.token"

    private fun providesMoshi() = MoshiConverterFactory.create(
        Moshi.Builder()
            .addLast(KotlinJsonAdapterFactory())
            .build()
    )

    private fun provideRefreshInterceptor(tokenPreferences: SharedPreferences) =
        OAuthInterceptor(
            Retrofit
                .Builder()
                .client(OkHttpClient.Builder().build())
                .baseUrl(BASE_URL)
                .addConverterFactory(providesMoshi())
                .build()
                .create(RefreshEndpoint::class.java), tokenPreferences
        )

    fun providePetEndpoints(): PetFinderEndpoint {

        return Retrofit.Builder()
            .client(
                OkHttpClient
                    .Builder()
                    .dispatcher(Dispatcher().apply { maxRequests = 5 })
                    .readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .addInterceptor(provideRefreshInterceptor(tokenPreferences(appContext!!)))
                    .build()
            )
            .baseUrl(BASE_URL)
            .addConverterFactory(providesMoshi())
            .build()
            .create(PetFinderEndpoint::class.java)
    }

    private fun tokenPreferences(app: Context) =
        app.getSharedPreferences(TOKEN_FILE_KEY, Context.MODE_PRIVATE)
}