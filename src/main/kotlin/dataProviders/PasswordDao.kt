package dataProviders

interface PasswordDao {
    fun showAccountList()
    fun createAccount(login: String, password: String) : Boolean
    fun checkPassword(login : String, password: String) : Boolean
    fun addPassword(login : String, password: String)
    fun updatePassword(login : String, oldPassword: String, newPassword: String) : Boolean
    fun deletePassword(login : String, password: String) : Boolean
}