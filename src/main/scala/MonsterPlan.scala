import unfiltered.request._
import unfiltered.response._

object MonsterShop extends App {

  val echo = unfiltered.filter.Planify {
    case Path(Seg("fisk" :: p :: Nil)) => ResponseString(p)
  }

  unfiltered.jetty.Server.local(8080).plan(echo).run()
}

object url {

}
