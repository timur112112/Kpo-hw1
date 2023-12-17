import kotlinx.serialization.Contextual
import kotlinx.datetime.LocalDateTime

@kotlinx.serialization.Serializable
class Session (private var film: Film, @Contextual private var time: LocalDateTime, private var tickets: Array<Array<SeatCondition>>) {
    private val FREE_SYMBOL = 'O'
    private val BUSY_SYMBOL = 'x'
    private val OCCUPIED_SYMBOL = '_'

    fun getDimensions(): Pair<Int, Int> {
        return Pair(tickets.size, tickets[0].size)
    }

    private fun getSeatSymbol(row: Int, seat: Int): Char {
        return when (tickets[row][seat]) {
            SeatCondition.FREE -> FREE_SYMBOL
            SeatCondition.SOLD -> BUSY_SYMBOL
            SeatCondition.OCCUPIED -> OCCUPIED_SYMBOL
        }
    }

    fun getFilm(): Film {
        return film
    }

    fun getTime(): LocalDateTime {
        return time
    }

    fun setTime(time: LocalDateTime) {
        this.time = time
    }

    fun setFilm(film: Film) {
        this.film = film
    }

    fun ticketIsFree(row: Int, seat: Int): Boolean {
        return tickets[row][seat] == SeatCondition.FREE
    }

    fun ticketIsSold(row: Int, seat: Int): Boolean {
        return tickets[row][seat] == SeatCondition.SOLD
    }

    fun ticketIsOccupied(row: Int, seat: Int): Boolean {
        return tickets[row][seat] == SeatCondition.OCCUPIED
    }

    fun buyTicket(row: Int, seat: Int) {
        tickets[row][seat] = SeatCondition.SOLD
    }

    fun returnTicket(row: Int, seat: Int) {
        tickets[row][seat] = SeatCondition.FREE
    }

    fun occupyTicket(row: Int, seat: Int) {
        tickets[row][seat] = SeatCondition.OCCUPIED
    }

    private fun ticketsToString(): String {
        var result = "\t\t"
        for (i in 1..tickets[0].size) {
            result += i
            result += "\t"
        }

        for (i in tickets.indices) {
            result += "\n"
            result += "\t"
            result += i + 1
            result += "\t"
            for (j in 0..<tickets[i].size) {
                result += getSeatSymbol(i, j) + "\t"
            }

        }
        return result
    }

    override fun toString(): String {
        return "\"${film.getTitle()}\" at ${time.toString()}\n" + ticketsToString()
    }
}