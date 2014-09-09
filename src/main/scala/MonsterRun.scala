import java.io.File

import org.eclipse.jetty.server.Handler
import org.eclipse.jetty.util.resource.ResourceCollection
import org.eclipse.jetty.webapp.WebAppContext
import unfiltered.jetty.Http

import scala.collection.mutable.ListBuffer

object MonsterRun extends App {

  val applications = Map[String, Handler](
    "billan"  -> webapp("/", "."))

  val apps = ListBuffer[String]() ++ applications.keys

  val http = Http(8080)

  for(app <- apps.map(applications)){
    http.handlers.addHandler(app)
  }

  http.run()

  def webapp(contextPath:String, location:String) = {
    val web = new WebAppContext()
    web.setContextPath(contextPath)
    web.setBaseResource(new ResourceCollection(Array(
      location + "/src/main/webapp",
      location + "/src/main/resources"
    )))
    web
  }
}
