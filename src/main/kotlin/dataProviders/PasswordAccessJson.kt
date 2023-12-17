package dataProviders

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.Closeable
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest

class PasswordAccessJson : PasswordDao, AutoCloseable {
    val path = "password.json"
    private var passwords = mutableListOf<Pair<String, String>>()

    init {
        try{
            val file = File(path)
            var content = file.readText()
            passwords = Json.decodeFromString<MutableList<Pair<String, String>>>(content)
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }
    }

    override fun close() {
        try {
            val file = File(path)
            val content = Json.encodeToString(passwords)
            file.writeBytes(content.toByteArray())
        } catch (e: Exception) {
            println("Error: ${e.message}")
        }

    }

    override fun showAccountList() {
        passwords.forEach { println(it.first) }
    }

    override fun createAccount(login: String, password: String): Boolean {
        if (passwords.find { it.first == login } == null) {
            passwords.add(Pair(login, hash(password)))
            return true
        }
        return false
    }

    override fun checkPassword(login: String, password: String) : Boolean {
        val passwordHash = hash(password)
        if (passwords.find { it == Pair(login, passwordHash) } != null) {
            return true
        }
        return false
    }

    override fun addPassword(login: String, password: String) {
        passwords.add(Pair(login, hash(password)))
    }

    override fun updatePassword(login: String, oldPassword: String, newPassword: String): Boolean {
        if (checkPassword(login, oldPassword)) {
            passwords.removeIf{it == Pair(login, hash(oldPassword))}
            passwords.add(Pair(login, hash(newPassword)))
            return true
        }
        return false

    }

    override fun deletePassword(login: String, password: String) : Boolean {
        if (checkPassword(login, password)) {
            passwords.removeIf{it == Pair(login, hash(password))}
            return true
        }
        return false
    }

    fun hash(password: String) : String {
        val md = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(password.toByteArray())
        val no = BigInteger(1, messageDigest)
        var hashtext = no.toString(16)
        while (hashtext.length < 32) {
            hashtext = "0$hashtext"
        }
        return hashtext
    }
}