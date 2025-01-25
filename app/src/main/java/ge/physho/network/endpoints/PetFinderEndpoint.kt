package ge.physho.network.endpoints

import ge.physho.model.specificanimal.Pets
import ge.physho.model.specificanimal.SpecificAnimal
import ge.physho.model.types.SpecificType
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PetFinderEndpoint {

    @GET("animals")
    suspend fun getAnimals(
        @Query("type") petType: String? = null,
        @Query("gender") gender: String? = null,
        @Query("coat") coat: String? = null,
        @Query("color") color: String? = null,
        @Query("page") pageNum: Int,
        @Query("limit") limit: Int = 12,
    ): Response<Pets>

    @GET("types/{pet}")
    suspend fun getAnimalTypes(
        @Path("pet") pet: String
    ): Response<SpecificType>

    @GET("animals/{id}")
    suspend fun getSpecificAnimal(
        @Path("id") petId: Int
    ): Response<SpecificAnimal>

}