package ge.physho.presentation.fragment.details

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ge.physho.model.specificanimal.SpecificAnimal
import ge.physho.network.NetworkModule
import ge.physho.network.state.Recourse
import ge.physho.network.state.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PetDetailsViewModel : ViewModel() {
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    val uiState: MutableLiveData<UiState> get() = _uiState

    fun specificPet(petId: Int) = getSpecificPet(petId)

    private suspend fun fetchSpecificAnimal(petId: Int): Recourse<SpecificAnimal> {
        return try {
            val response = NetworkModule.providePetEndpoints().getSpecificAnimal(petId)
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

    private fun getSpecificPet(petId: Int) {
        viewModelScope.launch {
            _uiState.value = UiState(isLoading = true)
            withContext(Dispatchers.IO) {
                when (val data = fetchSpecificAnimal(petId)) {
                    is Recourse.Success -> {
                        _uiState.postValue(data.data?.let {
                            UiState(
                                pet = it
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


}