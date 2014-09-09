import akka.actor.ActorSystem
import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request._
import unfiltered.response._

class MonsterPlan
  extends Plan
  with Monsters {
val system: ActorSystem = ActorSystem("foo")
  val spike = system.actorOf(Spike.props)

  override def intent: Intent = {
    case POST(Path( urls.basketPost(monsterType))) => spike !AddMonsterToBasket(BasketId("0") ,monsterByType(MonsterType(monsterType)))
      Ok

    case Path(Seg("fisk" :: p :: Nil)) => ResponseString(p)
  }
}

trait Monsters {
  val monsterTypes = Vector(
    Monster(MonsterType("Ao (skilpadde)"), Price(100000)),
    Monster(MonsterType("Bakeneko"), Price(120000)),
    Monster(MonsterType("Basilisk"), Price(175000)),
    Monster(MonsterType("Det erymanthiske villsvin"), Price(25000)),
    Monster(MonsterType("Griff"), Price(12000)),
    Monster(MonsterType("Hamløper"), Price(8000)),
    Monster(MonsterType("Hippogriff"), Price(128000)),
    Monster(MonsterType("Hydra"), Price(38000)),
    Monster(MonsterType("Kentaur"), Price(76000)),
    Monster(MonsterType("Kerberos"), Price(31000)),
    Monster(MonsterType("Kraken"), Price(2800)),
    Monster(MonsterType("Mannbjørn"), Price(49000)),
    Monster(MonsterType("Mantikora"), Price(21000)),
    Monster(MonsterType("Margyge"), Price(73000)),
    Monster(MonsterType("Marmæle"), Price(149000)),
    Monster(MonsterType("Minotauros"), Price(28000)),
    Monster(MonsterType("Nekomusume"), Price(62000)),
    Monster(MonsterType("Rokk"), Price(12000)),
    Monster(MonsterType("Seljordsormen"), Price(56000)),
    Monster(MonsterType("Sfinks"), Price(39000)),
    Monster(MonsterType("Sirene"), Price(12900)),
    Monster(MonsterType("Sjøorm"), Price(240000)),
    Monster(MonsterType("Succubus"), Price(84000)),
    Monster(MonsterType("Valravn"), Price(92300)),
    Monster(MonsterType("Vampyr"), Price(420000)),
    Monster(MonsterType("Varulv"), Price(69000)))

  val monsterByType: Map[MonsterType, Monster] =
    monsterTypes.map {
      monster => monster.monsterType -> monster
    }.toMap
}


case class BasketId(asString: String) extends AnyVal
case class Price(asInt: Int) extends AnyVal
case class OrderId(asString: String) extends AnyVal
case class MonsterType(asString: String) extends AnyVal

case class Monster(monsterType: MonsterType, price: Price)
case class Basket(id: BasketId, basketLines: Vector[BasketLine] = Vector.empty)
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
