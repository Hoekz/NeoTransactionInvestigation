import org.neo4j.driver.v1.Session
import scala.concurrent.duration._

object readNode {
  def apply(session: Session): Unit = {
    val deadline = 30.seconds.fromNow
    var goneCount = 0
    var runCount = 0
    var lastFound = 0

    while(deadline.hasTimeLeft) {
      runCount += 1
      session.readTransaction(queries.readNameFromNode) match {
        case None =>
          if (lastFound == runCount - 1) println("*****NOT FOUND*****")
          goneCount += 1
        case Some(_) =>
          if (lastFound != runCount - 1) println("FOUND")
          lastFound = runCount
      }
    }

    println(s"Missing ${goneCount - (runCount - lastFound)} / $lastFound times")
  }
}
