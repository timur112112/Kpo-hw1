package dataProviders

interface AnyDao {
    fun getObj(id: Int): Pair<Any, Int>
    fun getAllObjs(): List<Pair<Any, Int>>
    fun addObj(obj: Any)
    fun updateObj(id: Int, obj: Any)
    fun deleteObj(id: Int)
}