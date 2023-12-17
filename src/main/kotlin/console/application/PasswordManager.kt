package console.application

import dataProviders.PasswordDao

class PasswordManager(private val passwordDao: PasswordDao) {
    private var currentAccount: String? = null

    fun enterAccount() {
        while (true) {
            println("Enter login:")
            val login = readln()
            println("Enter password:")
            val password = readln()
            if (passwordDao.checkPassword(login, password)) {
                currentAccount = login
                println("You are logged in")
                break
            } else {
                println("Wrong login or password")
            }
        }
    }

    fun manageAccount() {
        while (true) {
            println("You are logged in as $currentAccount")
            println("1. Change password")
            println("2. Delete account")
            println("3. Exit account")
            if (currentAccount == "admin") {
                println("4. Create account")
                println("5. Show account list")
            }
            println("0. Exit")
            val command = getCommand()
            when (command) {
                1 -> changePassword()
                2 -> deleteAccount()
                3 -> exitAccount()
                4 -> if (currentAccount == "admin") {
                    println("Creating account")
                    println("Enter login:")
                    val login = readln()
                    println("Enter password:")
                    val password = readln()
                    if (passwordDao.createAccount(login, password)) {
                        println("Account created")
                    } else {
                        println("Account already exists")
                    }
                } else {
                    println("Unknown command")
                }

                5 -> if (currentAccount == "admin") {
                    println("Account list:")
                    passwordDao.showAccountList()
                } else {
                    println("Unknown command")
                }

                0 -> break
                else -> println("Unknown command")
            }
        }
    }

    private fun changePassword() {
        println("Changing password for $currentAccount")
        println("Enter old password:")
        val oldPassword = readln()
        println("Enter new password:")
        val newPassword = readln()
        if (passwordDao.updatePassword(currentAccount!!, oldPassword, newPassword)) {
            println("Password changed")
        } else {
            println("Wrong password")
        }
    }

    private fun deleteAccount() {
        println("Enter password:")
        val password = readln()
        if (passwordDao.deletePassword(currentAccount!!, password)) {
            println("Account deleted")
            exitAccount()
        } else {
            println("Wrong password")
        }
    }

    private fun exitAccount() {
        currentAccount = null
        println("You are logged out")
        enterAccount()
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