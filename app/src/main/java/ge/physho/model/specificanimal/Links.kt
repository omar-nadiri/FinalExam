package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Links(
    @Json(name = "organization")
    val organization: Organization?,
    @Json(name = "self")
    val self: Self?,
    @Json(name = "type")
    val type: Type?
)