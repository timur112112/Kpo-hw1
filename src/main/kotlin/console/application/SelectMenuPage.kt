package console.application

import dataProviders.AnyDao

class SelectMenuPage(
    private val menu: String,
    private val menuHolder: MenuHolder,
    private val connections: List<Int>,
    private val start: Int,
    private val dataProvider: AnyDao,
    private val editorMenu: EditorMenuInterface
) : MenuInterface {
    override var previousMenu: MenuInterface? = null

    override fun printMenu(): MenuInterface {
        println(menu)
        var i = start
        for (elem in dataProvider.getAllObjs()) {
            println("$i: ${elem.first.toString().split("\n")[0]}")
            ++i
        }
        return this
    }

    override fun selectOption(option: Int): MenuInterface? {
        return if (option < 0) {
            menuHolder.menus[connections[0]].setPrevMenu(this)
            menuHolder.menus[connections[0]]
        } else if (option == 0) {
            previousMenu
        } else if (option < start) {
            menuHolder.menus[connections[option]].setPrevMenu(this)
            menuHolder.menus[connections[option]]
        } else if (option < start + dataProvider.getAllObjs().size) {
            editorMenu.setEditingObject(
                dataProvider.getAllObjs()[option - start].first,
                dataProvider.getAllObjs()[option - start].second,
                dataProvider
            )
            editorMenu.setPrevMenu(this)
            editorMenu
        } else {
            menuHolder.menus[connections[0]].setPrevMenu(this)
            menuHolder.menus[connections[0]]
        }
    }
}