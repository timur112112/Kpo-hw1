package console.application

class MenuHolder {
    val menus = mutableListOf<MenuInterface>()
    fun addMenu(menu: MenuInterface) {
        menus.add(menu)
    }
}