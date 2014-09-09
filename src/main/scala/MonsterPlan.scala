import unfiltered.request._
import unfiltered.response._

object MonsterShop extends App {

  val echo = unfiltered.filter.Planify {
    case Path(Seg("fisk" :: p :: Nil)) => ResponseString(p)
  }

  unfiltered.jetty.Server.local(8080).plan(echo).run()
}

import linx._
object urls {
  //basketService
  val service = Root / "service"
  val basket = Root / "service" / "basket"
  val basketPost = basket / 'id
  val basketSump = basket / "sum"
  // orderService
  val orders = service / "orders"
  val order = orders / 'aggregateId

  // authService
  val authLogin = service / "auth" / "logIn"
  val authLogout = service / "auth" / "logOut"
  val authCustomer = service / "auth" / "customer"
  // monsterService
  val monsterTypes = service / "monsterTypes"
}
