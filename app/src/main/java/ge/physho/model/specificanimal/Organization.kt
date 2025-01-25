package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Organization(
    @Json(name = "href")
    val href: String?
)