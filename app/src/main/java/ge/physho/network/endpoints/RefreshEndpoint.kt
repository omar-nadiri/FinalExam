package ge.physho.network.endpoints

import ge.physho.model.specificanimal.TokenResponse
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RefreshEndpoint {
    @FormUrlEncoded
    @POST("oauth2/token")
    suspend fun getToken(
        @Field("grant_type") type: String = "client_credentials",
        @Field("client_id") key: String = "1tZrcm2ZGMSCkb0isgkX5GYCBtZA6Pm1iEaXJ0oBzLkIiMCo5s",
        @Field("client_secret") secret: String = "FhG7WZRUtxPG8s9Q21bbITmwJGR78fSflgPopUMa"
    ): Response<TokenResponse>
}