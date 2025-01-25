package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Animal(
    @Json(name = "age")
    val age: String?,
    @Json(name = "attributes")
    val attributes: Attributes?,
    @Json(name = "breeds")
    val breeds: Breeds?,
    @Json(name = "coat")
    val coat: String?,
    @Json(name = "colors")
    val colors: Colors?,
    @Json(name = "contact")
    val contact: Contact?,
    @Json(name = "description")
    val description: String?,
    @Json(name = "distance")
    val distance: Any?,
    @Json(name = "environment")
    val environment: Environment?,
    @Json(name = "gender")
    val gender: String?,
    @Json(name = "id")
    val id: Int?,
    @Json(name = "_links")
    val links: Links?,
    @Json(name = "name")
    val name: String?,
    @Json(name = "organization_animal_id")
    val organizationAnimalId: Any?,
    @Json(name = "organization_id")
    val organizationId: String?,
    @Json(name = "photos")
    val photos: MutableList<Photo>?,
    @Json(name = "primary_photo_cropped")
    val primaryPhotoCropped: PrimaryPhotoCropped?,
    @Json(name = "published_at")
    val publishedAt: String?,
    @Json(name = "size")
    val size: String?,
    @Json(name = "species")
    val species: String?,
    @Json(name = "status")
    val status: String?,
    @Json(name = "status_changed_at")
    val statusChangedAt: String?,
    @Json(name = "tags")
    val tags: List<String>?,
    @Json(name = "type")
    val type: String?,
    @Json(name = "url")
    val url: String?,
    @Json(name = "videos")
    val videos: List<Any>?
)