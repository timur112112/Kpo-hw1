package dataProviders
import Film

@kotlinx.serialization.Serializable
data class FilmHolder (var film: Film, var id: Int) {
}