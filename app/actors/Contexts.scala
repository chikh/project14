package actors

import play.api.libs.concurrent.Akka
import scala.concurrent.ExecutionContext
import play.api.Play.current

object Contexts {
  implicit val dbEmulationExecutionContext: ExecutionContext = Akka.system.dispatchers.lookup("db-emulation-context")
}
