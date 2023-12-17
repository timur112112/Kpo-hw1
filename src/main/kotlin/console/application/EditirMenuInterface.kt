package console.application

import dataProviders.AnyDao

interface EditorMenuInterface : MenuInterface {
    fun setEditingObject(obj: Any, id: Int, dataProvider: AnyDao)
}