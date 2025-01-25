package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Photo(
    @Json(name = "full")
    val full: String?,
    @Json(name = "large")
    val large: String?,
    @Json(name = "medium")
    val medium: String?,
    @Json(name = "small")
    val small: String?
)