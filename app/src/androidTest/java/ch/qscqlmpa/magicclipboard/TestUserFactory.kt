package ch.qscqlmpa.magicclipboard

data class TestUser(
    val email: String,
    val password: String
)

object TestUserFactory {
    val emulatorFirebaseUser1 = TestUser(
        email = "ned.stark@headless.com",
        password = "12345678"
    )
}
