import org.neo4j.driver.v1._

object Investigation {
  def main(args: Array[String]): Unit = {
    val driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", ""))
    val session = driver.session()

    if (args.headOption.contains("read-mode")) {
      readNode(session)
    } else if (args.headOption.contains("setup")) {
      session.writeTransaction(queries.createNode)
    } else {
      queries.delay = args.headOption.getOrElse("0").toInt
      destroyAndRecreateNode(session)
    }

    driver.close()
  }
}
