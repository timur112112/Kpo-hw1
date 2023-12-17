package console.application
import Film
import Session
import dataProviders.AnyDao
import dataProviders.FilmDao
import dataProviders.PasswordDao
import dataProviders.SessionDao
import kotlinx.datetime.LocalDateTime
import java.util.function.Function


class MenuCreator {
    fun getMenuHolder(filmDao: FilmDao, sessionDao: SessionDao, passwordDao: PasswordDao): MenuHolder {
        val passwordManager = PasswordManager(passwordDao)
        val menuHolder = MenuHolder()
        //0
        val unknownCommand = PrintMenu("Unknown command")
        //1
        val mainMenu = SimpleMenuPage(
            """
            Please, choose the action:
            1. Show all films
            2. Show all sessions
            3. Manage account settings
            0. Exit application
        """.trimIndent(), menuHolder, listOf(0, 2, 3, 4)
        )
        val filmEditor = EditorMenu(
            "Editing film %s\n0. Back\n1. Change title\n2. Delete film", menuHolder, listOf(0), listOf(
                Function { film: Any ->
                    if (film is Film) {
                        println("Enter new title:")
                        val title = readln(); if (title == "0") {
                            return@Function film; } else {
                            film.setTitle(title); return@Function film; }
                    } else {
                        return@Function film
                    }
                },
                Function { _: Any -> null })
        )
        //2
        val showFilms = SelectMenuPage(
            """
            0. Back
            1. Add new film
            All films:
        """.trimIndent(), menuHolder, listOf(0, 5), 2, filmDao, filmEditor
        )

        val sessionEditor =
            EditorMenu("Session %s\nSelect action:\n0. Back\n1. Sell ticket\n2. Return ticket\n3. Mark a customer\nEdit session:\n4. Change film\n5. Change time\n6. Delete session",
                menuHolder,
                listOf(0),
                listOf(
                    Function { session: Any ->
                        if (session is Session) {
                            val (row, seat) = getRowSeat(session.getDimensions().first, session.getDimensions().second)
                            if (session.ticketIsFree(row - 1, seat - 1)) {
                                session.buyTicket(row - 1, seat - 1)
                                println("Ticket sold.")
                                return@Function session
                            } else {
                                println("Ticket is not free")
                                return@Function session
                            }
                        } else {
                            return@Function session
                        }
                    },
                    Function { session: Any ->
                        if (session is Session) {
                            val (row, seat) = getRowSeat(session.getDimensions().first, session.getDimensions().second)
                            if (session.ticketIsSold(row - 1, seat - 1)) {
                                session.returnTicket(row - 1, seat - 1)
                                println("Ticket is returned")
                                return@Function session
                            } else {
                                if (session.ticketIsOccupied(row - 1, seat - 1)) {
                                    println("Place is occupied")
                                    return@Function session
                                } else {
                                    println("Ticket is not sold")
                                    return@Function session
                                }
                            }
                        } else {
                            return@Function session
                        }
                    },
                    Function { session: Any ->
                        if (session is Session) {
                            val (row, seat) = getRowSeat(session.getDimensions().first, session.getDimensions().second)
                            if (session.ticketIsSold(row - 1, seat - 1)) {
                                session.occupyTicket(row - 1, seat - 1)
                                return@Function session
                            } else {
                                if (session.ticketIsOccupied(row - 1, seat - 1)) {
                                    println("Place is occupied")
                                    return@Function session
                                } else {
                                    println("Ticket is not sold")
                                    return@Function session
                                }
                            }
                        } else {
                            return@Function session
                        }
                    },
                    Function { session: Any ->
                        if (session is Session) {
                            val film: Film
                            val command = selectFilm(filmDao)
                            if (command == 0) {
                                return@Function session
                            } else {
                                film = filmDao.getAllFilms()[command - 1].first
                            }
                            session.setFilm(film)
                            println("Film changed")
                            return@Function session
                        } else {
                            return@Function session
                        }
                    },
                    Function { session: Any ->
                        if (session is Session) {
                            var time: String
                            while (true) {
                                println("Enter date and time (yyyy-MM-dd HH:mm):")
                                time = readln().replace(" ", "T")
                                if (time == "0") {
                                    return@Function session
                                }
                                try {
                                    LocalDateTime.parse(time)
                                    break
                                } catch (e: Exception) {
                                    println("Wrong date format")
                                }
                            }
                            session.setTime(LocalDateTime.parse(time))
                            println("Time changed")
                            return@Function session
                        } else {
                            return@Function session
                        }
                    },
                    Function { _: Any ->
                        null
                    }
                ))

        //3
        val showSessions = SelectMenuPage(
            """
            0. Back
            1. Add new session
            All session:
        """.trimIndent(), menuHolder, listOf(0, 6), 2, sessionDao, sessionEditor
        )

        //4
        val passwordMenu = CallBackMenu({ _: Any -> passwordManager.manageAccount() }, sessionDao)
        //5
        val addFilmMenu = CallBackMenu(Function { anyDao: AnyDao ->
            println("Enter film title:")
            val title = readln()
            if (title == "0") {
                return@Function
            }
            val film = Film(title)
            anyDao.addObj(film)
            println("Film added")
        }, filmDao)
        //6
        val addSessionMenu = CallBackMenu(Function { anyDao: AnyDao ->
            val film: Film
            var time: String
            val command = selectFilm(filmDao)
            if (command == 0) {
                return@Function
            } else {
                film = filmDao.getAllFilms()[command - 1].first
            }

            while (true) {
                println("Enter date and time (yyyy-MM-dd HH:mm):")
                time = readln().replace(" ", "T")
                if (time == "0") {
                    return@Function
                }
                try {
                    LocalDateTime.parse(time)
                    break
                } catch (e: Exception) {
                    println("Wrong date format")
                }
            }

            val session = Session(film, LocalDateTime.parse(time), Array(3) { Array(3) { SeatCondition.FREE } })
            anyDao.addObj(session)
            println("Session added")
        }, sessionDao)
        menuHolder.addMenu(unknownCommand)
        menuHolder.addMenu(mainMenu)
        menuHolder.addMenu(showFilms)
        menuHolder.addMenu(showSessions)
        menuHolder.addMenu(passwordMenu)
        menuHolder.addMenu(addFilmMenu)
        menuHolder.addMenu(addSessionMenu)

        val startMenu = CallBackMenu({ _: AnyDao ->
            passwordManager.enterAccount()
        }, sessionDao)
        startMenu.setPrevMenu(mainMenu)

        menuHolder.addMenu(startMenu)
        return menuHolder
    }

    private fun selectFilm(filmDao: FilmDao) : Int {
        val filmList = filmDao.getAllFilms()
        println("Select film:")
        println("0. Back")
        if (filmList.isEmpty()) {
            println("No films")
        } else {
            var i = 1
            for (film in filmList) {
                println("$i. \"${film.first.getTitle()}\"")
                i++
            }
        }
        var command = getCommand()
        while (command < 0 || command > filmList.size) {
            println("Unknown command")
            command = getCommand()
        }
        return command
    }

    private fun getCommand(): Int {
        var command = readln()
        while (true) {
            try {
                return command.toInt()
            } catch (e: Exception) {
                println("Unknown command")
                command = readln()
            }
        }
    }

    private fun getRowSeat(rows: Int, seats: Int): Pair<Int, Int> {
        println("Enter row:")
        var row = getCommand()
        while (row < 1 || row > rows) {
            println("Unknown row")
            row = getCommand()
        }
        println("Enter seat:")
        var seat = getCommand()
        while (seat < 1 || seat > seats) {
            println("Unknown seat")
            seat = getCommand()
        }
        return Pair(row, seat)
    }
}