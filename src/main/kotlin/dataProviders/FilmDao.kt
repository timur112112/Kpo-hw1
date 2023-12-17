package dataProviders
import Film

interface FilmDao : AnyDao {
    fun getFilm(id: Int): Pair<Film, Int>
    fun getAllFilms(): List<Pair<Film, Int>>
    fun addFilm(film: Film)
    fun updateFilm(id: Int, film: Film)
    fun deleteFilm(id: Int)

    override fun getObj(id: Int): Pair<Any, Int> {
        return getFilm(id)
    }
    override fun getAllObjs(): List<Pair<Any, Int>> {
        return getAllFilms()
    }
    override fun addObj(obj: Any) {
        if (obj is Film) addFilm(obj)
        else throw Exception("Wrong type of object")
    }
    override fun updateObj(id: Int, obj: Any) {
        if (obj is Film) updateFilm(id, obj)
        else throw Exception("Wrong type of object")
    }
    override fun deleteObj(id: Int) {
        deleteFilm(id)
    }
}

