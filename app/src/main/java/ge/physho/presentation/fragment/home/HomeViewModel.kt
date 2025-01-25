package ge.physho.presentation.fragment.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import ge.physho.model.NewsItem
import ge.physho.model.types.SpecificType
import ge.physho.network.NetworkModule
import ge.physho.network.state.Recourse
import ge.physho.network.state.UiState
import ge.physho.presentation.fragment.home.source.PetDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel : ViewModel() {

    var gender: String? = null
    var coat: String? = null
    var color: String? = null
    var pet: String? = null

    val pets = getUserResponse()
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    val uiState: MutableLiveData<UiState> get() = _uiState

    private fun getUserResponse() =
        Pager(
            config = PagingConfig(
                pageSize = 1,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { PetDataSource(pet, gender, coat, color) }
        ).flow.cachedIn(
            viewModelScope
        )

    private suspend fun fetchSpecificType(pet: String): Recourse<SpecificType> {
        return try {
            val response = NetworkModule.providePetEndpoints().getAnimalTypes(pet)
            val result = response.body()
            if (response.isSuccessful && result != null) {
                Recourse.Success(result)
            } else {
                Recourse.Error("API Error !")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            Recourse.Error("Something went wrong !")
        }
    }

    fun getPetTypes(pet: String) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            withContext(Dispatchers.IO) {
                when (val data = fetchSpecificType(pet)) {
                    is Recourse.Success -> {
                        _uiState.postValue(data.data?.let {
                            UiState(
                                animalTypes = it.type
                            )
                        })
                    }
                    is Recourse.Error -> {
                        _uiState.postValue(UiState(error = UiState.Error.NETWORK_ERROR))
                    }
                }
            }
        }
    }


    val newsItemList: List<String> = listOf(
        "dog",
        "cat",
        "Rabbit",
        "Barnyard",
        "Small & Furry",
    )
}