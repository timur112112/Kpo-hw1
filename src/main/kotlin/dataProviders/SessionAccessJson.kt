package dataProviders

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.io.File

class SessionAccessJson : SessionList(), AutoCloseable {
    val path = "sessions.json"

    init {
        try{
            val file = File(path)
            var content = file.readText()
            sessions = Json.decodeFromString<MutableList<SessionHolder>>(content)
            id = sessions.maxOf { it.id } + 1
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override fun close() {
        val file = File(path)
        val content = Json.encodeToString(sessions)
        file.writeBytes(content.toByteArray())
    }
}