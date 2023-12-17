package console.application

class SimpleMenuPage(private val menu: String, private val menuHolder: MenuHolder, private val connections: List<Int>) : MenuInterface {
    override var previousMenu: MenuInterface? = null
    override fun printMenu(): MenuInterface {
        println(menu)
        return this
    }

    override fun selectOption(option: Int): MenuInterface? {
        if (option < 0) {
            menuHolder.menus[connections[0]].setPrevMenu(this)
            return menuHolder.menus[connections[0]]
        }
        if (option == 0) {
            return previousMenu
        } else if (option < connections.size) {
            menuHolder.menus[connections[option]].setPrevMenu(this)
            return menuHolder.menus[connections[option]]
        }
        menuHolder.menus[connections[0]].setPrevMenu(this)
        return menuHolder.menus[connections[0]]
    }
}