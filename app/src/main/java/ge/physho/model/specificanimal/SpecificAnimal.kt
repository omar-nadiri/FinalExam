package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class SpecificAnimal(
    @Json(name = "animal")
    val animal: Animal?
)