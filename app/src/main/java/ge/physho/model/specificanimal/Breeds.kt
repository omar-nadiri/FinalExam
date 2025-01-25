package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Breeds(
    @Json(name = "mixed")
    val mixed: Boolean?,
    @Json(name = "primary")
    val primary: String?,
    @Json(name = "secondary")
    val secondary: Any?,
    @Json(name = "unknown")
    val unknown: Boolean?
)