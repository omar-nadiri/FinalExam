package ge.physho.presentation.fragment.home.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ge.physho.model.specificanimal.Animal
import ge.physho.model.specificanimal.Pets
import ge.physho.network.NetworkModule
import ge.physho.network.state.Recourse

const val USER_STARTING_PAGE_INDEX = 1

class PetDataSource(
    private val pet: String?,
    private val gender: String?,
    private val coat: String?,
    private val color: String?
) :
    PagingSource<Int, Animal>() {

    private suspend fun fetchPets(page: Int): Recourse<Pets> {
        return try {
            val response =
                NetworkModule.providePetEndpoints().getAnimals(pet, gender, coat, color, page)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Recourse.Success(result)
            } else {
                Recourse.Error(response.errorBody().toString())
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Recourse.Error("Sorry, something went wrong !")
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Animal> {
        val position = params.key ?: USER_STARTING_PAGE_INDEX

        return when (val pets = fetchPets(position)) {
            is Recourse.Success -> {
                LoadResult.Page(
                    data = pets.data!!.animals,
                    prevKey = if (position == USER_STARTING_PAGE_INDEX) null else position - 1,
                    nextKey = if (pets.data.animals.isEmpty()) null else position + 1
                )
            }
            is Recourse.Error -> {
                LoadResult.Error(Throwable())
            }
        }
    }


    override fun getRefreshKey(state: PagingState<Int, Animal>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey
        }
    }

}