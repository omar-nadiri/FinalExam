package ge.physho.network.state

import ge.physho.model.specificanimal.Animal
import ge.physho.model.specificanimal.SpecificAnimal
import ge.physho.model.types.SpecificType

data class UiState(
    val isLoading: Boolean = false,
    val error: Error? = null,
    val animalData: List<Animal> = listOf(),
    val animalTypes: SpecificType.Type? = null,
    val pet: SpecificAnimal? = null,
) {
    enum class Error(val message: String) {
        NETWORK_ERROR("Network Error"),
    }
}
