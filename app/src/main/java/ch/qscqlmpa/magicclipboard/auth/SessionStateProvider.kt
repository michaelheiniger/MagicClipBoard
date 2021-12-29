package ch.qscqlmpa.magicclipboard.auth

interface SessionStateProvider {
    val userId: UserId?
}
