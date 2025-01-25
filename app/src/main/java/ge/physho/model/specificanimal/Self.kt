package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Self(
    @Json(name = "href")
    val href: String?
)