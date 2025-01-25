package ge.physho.model


import com.squareup.moshi.Json
import ge.physho.model.specificanimal.LinksX

data class Pagination(
    @Json(name = "count_per_page")
    val countPerPage: Int?,
    @Json(name = "current_page")
    val currentPage: Int?,
    @Json(name = "_links")
    val links: LinksX?,
    @Json(name = "total_count")
    val totalCount: Int?,
    @Json(name = "total_pages")
    val totalPages: Int?
)