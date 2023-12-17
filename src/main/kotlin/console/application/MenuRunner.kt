package console.application

class MenuRunner(menuHolder: MenuHolder, start: Int) {
    private var currentMenu: MenuInterface? = menuHolder.menus[start]
    fun run() {
        while (true) {
            val newMenu = currentMenu!!.printMenu()
            if (newMenu != currentMenu) {
                currentMenu = newMenu
                continue
            }
            val command = getCommand()
            currentMenu = currentMenu!!.selectOption(command)
            if (currentMenu == null) {
                return
            }
        }
    }

    private fun getCommand(): Int {
        var command = readln()
        while (true) {
            try {
                return command.toInt()
            } catch (e: Exception) {
                println("Unknown command")
                command = readln()
            }
        }
    }
}