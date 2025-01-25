package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Attributes(
    @Json(name = "declawed")
    val declawed: Boolean?,
    @Json(name = "house_trained")
    val houseTrained: Boolean?,
    @Json(name = "shots_current")
    val shotsCurrent: Boolean?,
    @Json(name = "spayed_neutered")
    val spayedNeutered: Boolean?,
    @Json(name = "special_needs")
    val specialNeeds: Boolean?
)