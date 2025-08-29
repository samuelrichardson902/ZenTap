package com.example.zentap.data


/**
 * A data class to hold the properties of a splash message on the block screen.
 */
data class BlockedMessage(
    val emoji: String,
    val text: String
)

/**
 * Provides a list of default messages and a function to get a random one.
 */
object BlockedMessages {

    private val messages = listOf(
        BlockedMessage("🦖", "%s went extinct."),
        BlockedMessage("🚀", "%s is lost in space."),
        BlockedMessage("😴", "%s is sleeping."),
        BlockedMessage("👀", "Nothing to see at %s."),
        BlockedMessage("☕️", "%s is on a coffee break."),
        BlockedMessage("🎣", "%s has gone fishing."),
        BlockedMessage("🚧", "%s is under construction."),
        BlockedMessage("👻", "%s has ghosted you.")
    )

    fun getRandomMessage(): BlockedMessage {
        return messages.random()
    }
}
