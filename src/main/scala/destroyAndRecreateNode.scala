import org.neo4j.driver.v1.Session
import scala.concurrent.duration._

object destroyAndRecreateNode {
  def apply(session: Session): Unit = {
    session.writeTransaction(queries.createNode)

    val deadline = 30.seconds.fromNow

    while(deadline.hasTimeLeft) {
      session.writeTransaction(queries.deleteAndCreateNode)
    }

    session.writeTransaction(queries.deleteNode)
  }
}
