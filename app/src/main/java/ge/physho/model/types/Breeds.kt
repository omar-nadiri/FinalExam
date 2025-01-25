package ge.physho.model.types


import com.squareup.moshi.Json

data class Breeds(
    @Json(name = "href")
    val href: String?
)