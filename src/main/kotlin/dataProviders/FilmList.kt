package dataProviders

import Film

open class FilmList() : FilmDao {
    protected var films = mutableListOf<FilmHolder>()
    protected var id = 0
    override fun getFilm(id: Int): Pair<Film, Int> {
        return Pair(films.find { it.id == id }!!.film, id)
    }

    override fun getAllFilms(): List<Pair<Film, Int>> {
        return films.map { Pair(it.film, it.id) }
    }

    override fun addFilm(film: Film) {
        films.add(FilmHolder(film, id))
        id++
    }

    override fun updateFilm(id: Int, film: Film) {
        val filmHolder = films.find { it.id == id }!!
        filmHolder.film = film
    }

    override fun deleteFilm(id: Int) {
        films.removeIf { it.id == id }
    }
}

