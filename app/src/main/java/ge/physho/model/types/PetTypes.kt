package ge.physho.model.types


import com.squareup.moshi.Json

data class PetTypes(
    @Json(name = "types")
    val types: List<SpecificType.Type>?
)