package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Colors(
    @Json(name = "primary")
    val primary: String?,
    @Json(name = "secondary")
    val secondary: String?,
    @Json(name = "tertiary")
    val tertiary: Any?
)