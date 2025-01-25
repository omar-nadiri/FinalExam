package ge.physho.model.specificanimal


import com.squareup.moshi.Json
import ge.physho.model.Pagination

data class Pets(
    @Json(name = "animals")
    val animals: List<Animal>,
    @Json(name = "pagination")
    val pagination: Pagination?
)