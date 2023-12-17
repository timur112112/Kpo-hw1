import console.application.MenuCreator
import console.application.MenuRunner
import dataProviders.FilmAccessJson
import dataProviders.SessionAccessJson



fun main(args: Array<String>) {
    val filmList = FilmAccessJson()
    val sessionList = SessionAccessJson()
    val passwordAccessJson = dataProviders.PasswordAccessJson()
    try {
        val menuCreator = MenuCreator()
        val menuHolder =  menuCreator.getMenuHolder(filmList, sessionList, passwordAccessJson)
        val menuRunner = MenuRunner(menuHolder, 7)
        menuRunner.run()
    } finally {
        filmList.close()
        sessionList.close()
        passwordAccessJson.close()
    }

}

