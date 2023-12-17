package console.application

import dataProviders.AnyDao
import java.util.function.Function

class EditorMenu(
    private val menu: String,
    private val menuHolder: MenuHolder,
    private val connections: List<Int>,
    private val editors: List<Function<Any, Any?>>
) : EditorMenuInterface {
    override var previousMenu: MenuInterface? = null
    private lateinit var editingObj: Any
    var id: Int = 0
    private lateinit var dataProvider: AnyDao
    override fun setEditingObject(obj: Any, id: Int, dataProvider: AnyDao) {
        editingObj = obj
        this.id = id
        this.dataProvider = dataProvider
    }

    override fun printMenu(): MenuInterface {
        println(String.format(menu, editingObj.toString()))
        return this
    }

    override fun selectOption(option: Int): MenuInterface? {
        return if (option < 0 || option > editors.size) {
            menuHolder.menus[connections[0]].setPrevMenu(this)
            menuHolder.menus[connections[0]]
        } else if (option == 0) {
            previousMenu
        } else {
            val t = editors[option - 1].apply(editingObj)
            if (t == null) {
                dataProvider.deleteObj(id)
            } else {
                dataProvider.updateObj(id, t)
            }
            this
        }
    }

}