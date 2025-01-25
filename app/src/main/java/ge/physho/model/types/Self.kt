package ge.physho.model.types


import com.squareup.moshi.Json

data class Self(
    @Json(name = "href")
    val href: String?
)