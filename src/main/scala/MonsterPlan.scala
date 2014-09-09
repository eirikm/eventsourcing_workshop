import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request._
import unfiltered.response._

class MonsterPlan extends Plan {

  override def intent: Intent = {
    case Path(Seg("fisk" :: p :: Nil)) => ResponseString(p)
  }
}

case class BasketId(asString: String) extends AnyVal
case class Price(asLong: Long) extends AnyVal
case class OrderId(asString: String) extends AnyVal

sealed trait MonsterType
case class Monster(monsterType: MonsterType)
case class Basket(id: BasketId, baskeLines: Vector[BasketLine])
case class BasketLine(monsterType: MonsterType, price: Price, amount: Int)

case class OrderConfirmation(orderId: OrderId)
case class Order(orderId: OrderId, lines: Vector[OrderLine])
case class OrderLine(monsterType: MonsterType, price: Price, amount: Int)

import linx._
object urls {
  //basketService
  val service = Root / "service"
  val basket = Root / "service" / "basket"
  val basketPost = basket / 'id
  val basketSum = basket / "sum"

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
