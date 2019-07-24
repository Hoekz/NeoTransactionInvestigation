import java.util

import org.neo4j.driver.v1._

object queries {
  var delay = 0

  def createNode(tx: Transaction) = {
    val result = tx.run("CREATE (a:Investigation { id: '1234567890', name: 'test' }) RETURN a")
    result.single.get(0)
  }

  def readNameFromNode(tx: Transaction) = {
    val result: StatementResult = tx.run("MATCH (a:Investigation { id: '1234567890' }) RETURN a")

    val maybeName: util.List[Record] = result.list()

    if (maybeName.isEmpty) None else Some(maybeName.get(0).get(0))
  }

  def deleteAndCreateNode(tx: Transaction) = {
    deleteNode(tx)
    Thread.sleep(delay)
    createNode(tx)
  }

  def deleteNode (tx: Transaction) = {
    tx.run("MATCH (a:Investigation { id: '1234567890' }) DELETE a")
  }
}
