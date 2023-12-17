package dataProviders
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.io.File

class FilmAccessJson : FilmList(), AutoCloseable{
    private val path = "films.json"

    init {
        try{
            val file = File(path)
            val content = file.readText()
            films = Json.decodeFromString<MutableList<FilmHolder>>(content)
            id = films.maxOf { it.id } + 1
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override fun close() {
        val file = File(path)
        val content = Json.encodeToString(films)
        file.writeBytes(content.toByteArray())
    }
}