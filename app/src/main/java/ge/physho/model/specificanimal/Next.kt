package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Next(
    @Json(name = "href")
    val href: String?
)