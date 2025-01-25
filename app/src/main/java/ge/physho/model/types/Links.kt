package ge.physho.model.types


import com.squareup.moshi.Json

data class Links(
    @Json(name = "breeds")
    val breeds: Breeds?,
    @Json(name = "self")
    val self: Self?
)