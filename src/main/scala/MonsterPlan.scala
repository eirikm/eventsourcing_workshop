import akka.actor.ActorSystem
import akka.persistence.Update
import akka.util.Timeout
import unfiltered.filter.Plan
import unfiltered.filter.Plan.Intent
import unfiltered.request._
import unfiltered.response._
import akka.pattern.ask

import linx._

import scala.concurrent.duration._
import scala.concurrent.{Await, Future}

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
  val authLogin = service / "auth" / "logIn" / 'username
  val authLogout = service / "auth" / "logOut"
  val authCustomer = service / "auth" / "customer"

  // monsterService
  val monsterTypes = service / "monsterTypes"
}

class MonsterPlan
  extends Monsters
  with Plan
  with MockData
{
  val system: ActorSystem = ActorSystem("foo")
  val spike = system.actorOf(Spike.props)
  val basketView = system.actorOf(BasketView.props)

  import system.dispatcher
  import BasketViewProtocol._

  override def intent: Intent = {
    case GET(Path(urls.authCustomer())) => Ok ~> ResponseString(customerJson)

    case GET(Path(urls.authLogout())) => Ok

    case GET(Path(urls.basket())) =>
      val basketFuture: Future[BasketViewProtocol.Basket] =
        ask(basketView, GetBasket(BasketId("0")))(Timeout(5.seconds)).mapTo[BasketViewProtocol.Basket]

      val basketFut2 = basketFuture.map {
        case BasketViewProtocol.Basket(_, basketMap) =>
          basketMap.values.map {
            (line: BasketLine) =>
              s""" "${line.monsterType.asString}": ${line.toJson} """
          }.mkString("{", ",", "}")
      }
      val basketJson = Await.result(basketFut2, Duration.Inf)
      println(basketJson)
      Ok ~> ResponseString(basketJson) ~> JsonContent

    case GET(Path(urls.monsterTypes())) => Ok ~> ResponseString(monsterTypesAsJson)

    case GET(Path(urls.basketSum())) =>
      val sumFut = ask(basketView, GetSum(BasketId("0")))(Timeout(5.seconds)).mapTo[Sum]
      val sum = Await.result(sumFut, Duration.Inf)
      val sumJson = s"""{ "sum": ${sum.sum} }"""

      Ok ~> ResponseString(sumJson)

    case GET(Path(urls.orders())) => Ok ~> ResponseString(makeMockOrders)

    case POST(Path(urls.authLogin(username))) => Ok

    case POST(Path(Seg("service" :: "basket" :: monsterTypeName :: Nil))) =>
      spike ! AddMonsterToBasket(BasketId("0"), monsterByType(MonsterType(monsterTypeName)))
      Thread.sleep(50)
      Ok

    case DELETE(Path(urls.basketPost(monsterTypeName))) =>
      spike ! RemoveMonsterFromBasket(BasketId("0"), monsterByType(MonsterType(monsterTypeName)))
      Thread.sleep(50)
      Ok
  }
}

trait Monsters {

  val monsterTypes = Vector(
    Monster(MonsterType("Ao_(skilpadde)"), Price(100000)),
    Monster(MonsterType("Bakeneko"), Price(120000)),
    Monster(MonsterType("Basilisk"), Price(175000)),
    Monster(MonsterType("Det_erymanthiske_villsvin"), Price(25000)),
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

  val monsterTypesAsJson: String = monsterTypes.map {
    monster =>
      s"""{ "name": "${monster.monsterType.asString}", "price": ${monster.price.asInt} }"""
  }.mkString("[", ",", "]")

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
case class BasketLine(monsterType: MonsterType, price: Price, amount: Int) {
  def toJson =
    s"""{ "name": "${monsterType.asString}", "number": "$amount", "price": "$price" }"""
}

case class OrderConfirmation(orderId: OrderId)
case class Order(orderId: OrderId, lines: Vector[OrderLine])
case class OrderLine(monsterType: MonsterType, price: Price, amount: Int)

trait MockData {
  val makeMockOrders = """
                         |{"foo123" : {"date" : "2014-01-01",
                         |"sum" : 1000,
                         |"orderLineItems" : [
                         |{"name" : "Olav Thon", "number": 4, "price" : 10000 }
                         |]
                         |}}
                       """.stripMargin

  val basketJson: String = """
                             |{ "Ao_(skilpadde)" :
                             | { "name" : "foo",
                             |   "number" : 1 ,
                             |   "price" : 100
                             | }
                             |}
                           """.stripMargin
  val customerJson: String = """{ "customerName": "rulle"}"""
  val sumJson: String = """{"sum" : 100000}"""
}
