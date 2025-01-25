package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class LinksX(
    @Json(name = "next")
    val next: Next?
)