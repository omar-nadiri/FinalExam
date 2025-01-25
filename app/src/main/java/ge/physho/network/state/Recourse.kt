package ge.physho.network.state

sealed class Recourse<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T): Recourse<T>(data)
    class Error<T>(message: String, data: T? = null): Recourse<T>(data, message)
}