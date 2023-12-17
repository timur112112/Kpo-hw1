package dataProviders

import Session

interface SessionDao : AnyDao {
    fun getSession(id: Int): Pair<Session, Int>
    fun getAllSessions(): List<Pair<Session, Int>>
    fun addSession(session: Session)
    fun updateSession(id: Int, session: Session)
    fun deleteSession(id: Int)

    override fun getObj(id: Int): Pair<Any, Int> {
        return getSession(id)
    }
    override fun getAllObjs(): List<Pair<Any, Int>> {
        return getAllSessions()
    }
    override fun addObj(obj: Any) {
        if (obj is Session) addSession(obj)
        else throw Exception("Wrong type of object")
    }
    override fun updateObj(id: Int, obj: Any) {
        if (obj is Session) updateSession(id, obj)
        else throw Exception("Wrong type of object")
    }
    override fun deleteObj(id: Int) {
        deleteSession(id)
    }
}