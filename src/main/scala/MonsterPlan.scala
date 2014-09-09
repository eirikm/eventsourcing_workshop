import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request._
import unfiltered.response._

class MonsterPlan extends Plan {

  override def intent: Intent = {
    case Path(Seg("fisk" :: p :: Nil)) => ResponseString(p)
  }
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
