package ge.physho.model.types


import com.squareup.moshi.Json

data class SpecificType(
    @Json(name = "type")
    val type: Type?
) {
    data class Type(
        @Json(name = "coats")
        val coats: List<String>?,
        @Json(name = "colors")
        val colors: List<String>?,
        @Json(name = "genders")
        val genders: List<String>?,
        @Json(name = "_links")
        val links: Links?,
        @Json(name = "name")
        val name: String?
    )
}