package ge.physho.model.specificanimal


import com.squareup.moshi.Json

data class Contact(
    @Json(name = "address")
    val address: Address?,
    @Json(name = "email")
    val email: String?,
    @Json(name = "phone")
    val phone: String?
)