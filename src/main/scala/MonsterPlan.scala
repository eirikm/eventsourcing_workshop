import unfiltered.request._
import unfiltered.response._

object MonsterShop extends App {

  val echo = unfiltered.filter.Planify {
    case Path(Seg(p :: Nil)) => ResponseString(p)
  }

  unfiltered.jetty.Server.anylocal.plan(echo).run()
}

