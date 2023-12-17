package console.application

import dataProviders.AnyDao
import java.util.function.Function

class CallBackMenu(private val func: Function<AnyDao, Unit>, private val dataProvider: AnyDao) : MenuInterface {
    override var previousMenu: MenuInterface? = null

    override fun printMenu(): MenuInterface? {
        func.apply(dataProvider)
        return previousMenu
    }

    override fun selectOption(option: Int): MenuInterface? {
        TODO("Shouldn't be called")
    }

}