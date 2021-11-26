package ch.qscqlmpa.magicclipboard.data

fun <T> ResultWithData<T>.data(): T {
    return when (this) {
        is ResultWithData.Error -> org.junit.jupiter.api.fail("Wrong type: Success with data expected")
        is ResultWithData.Success -> this.data
    }
}
