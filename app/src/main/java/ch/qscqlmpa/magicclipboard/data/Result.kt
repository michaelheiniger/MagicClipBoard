package ch.qscqlmpa.magicclipboard.data

sealed class Result {
    object Success : Result()
    data class Error(val exception: Exception) : Result()
}

sealed class ResultWithData<out R> {
    data class Success<out T>(val data: T) : ResultWithData<T>()
    data class Error(val exception: Exception) : ResultWithData<Nothing>()
}
