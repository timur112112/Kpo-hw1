package dataProviders

import Session

open class SessionList : SessionDao {
    protected var sessions = mutableListOf<SessionHolder>()
    protected var id = 0
    override fun getSession(id: Int): Pair<Session, Int> {
        return Pair(sessions.find { it.id == id }!!.session, id)
    }

    override fun getAllSessions(): List<Pair<Session, Int>> {
        return sessions.map { Pair(it.session, it.id) }
    }

    override fun addSession(session: Session) {
        sessions.add(SessionHolder(session, id))
        id++
    }

    override fun updateSession(id: Int, session: Session) {
        val sessionHolder = sessions.find { it.id == id }!!
        sessionHolder.session = session
    }

    override fun deleteSession(id: Int) {
        sessions.removeIf { it.id == id }
    }
}

