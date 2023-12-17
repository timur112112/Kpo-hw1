@kotlinx.serialization.Serializable
class Film(private var title: String) {
    fun getTitle(): String {
        return title
    }

    fun setTitle(title: String) {
        this.title = title
    }

    override fun toString(): String {
        return "\"$title\""
    }
}