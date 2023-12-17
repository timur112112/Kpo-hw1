package console.application

interface MenuInterface {
    var previousMenu: MenuInterface?
    fun setPrevMenu(menu: MenuInterface) {
        previousMenu = menu
    }


    fun printMenu(): MenuInterface?
    fun selectOption(option: Int): MenuInterface?
}