package dataProviders
import Session

@kotlinx.serialization.Serializable
data class SessionHolder (var session: Session, var id: Int) {
}