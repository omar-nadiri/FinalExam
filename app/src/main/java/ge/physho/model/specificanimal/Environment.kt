package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Environment(
    @Json(name = "cats")
    val cats: Boolean?,
    @Json(name = "children")
    val children: Any?,
    @Json(name = "dogs")
    val dogs: Any?
)