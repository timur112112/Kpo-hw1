package console.application

class PrintMenu(private val menu: String) : MenuInterface {
    override var previousMenu: MenuInterface? = null
    override fun printMenu(): MenuInterface? {
        println(menu)
        return previousMenu
    }

    override fun selectOption(option: Int): MenuInterface? {
        TODO("Not yet implemented")
    }

}