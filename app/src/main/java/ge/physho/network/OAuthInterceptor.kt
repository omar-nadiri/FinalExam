package ge.physho.network

import android.content.SharedPreferences
import android.util.Log
import ge.physho.network.endpoints.RefreshEndpoint
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response

class OAuthInterceptor(
    private val refreshEndpoint: RefreshEndpoint,
    private val tokenPreferences: SharedPreferences
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        synchronized(this) {
            val token = tokenPreferences.getString("token", null)
            val originalRequest = chain.request()
            val authenticationRequest =
                originalRequest.newBuilder().addHeader("Authorization", "Bearer $token").build()
            val initialResponse = chain.proceed(authenticationRequest)

            return when (initialResponse.code) {
                401 -> {
                    val responseNewTokenLoginModel = runBlocking { refreshEndpoint.getToken() }

                    if (responseNewTokenLoginModel.isSuccessful) {
                        val newToken = responseNewTokenLoginModel.body()?.accessToken?.let { t ->
                            tokenPreferences.edit().putString("token", t).apply()
                            t
                        }
                        val newAuthenticationRequest = originalRequest.newBuilder()
                            .addHeader("Authorization", "Bearer $newToken").build()
                        initialResponse.close()
                        chain.proceed(newAuthenticationRequest)
                    } else initialResponse
                }
                else -> initialResponse
            }
        }
    }
}