package ge.physho.model.specificanimal

import com.squareup.moshi.Json

data class Type(
    @Json(name = "href")
    val href: String?
)