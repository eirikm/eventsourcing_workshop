import akka.actor.Props
import akka.persistence.PersistentView

object BasketViewProtocol {
  sealed trait Query
  case class GetSum(id: BasketId) extends Query
  case class GetBasket(id: BasketId) extends Query

  sealed trait Result
  case class Sum(id: BasketId, sum: Int) extends Result
  case class Basket(id: BasketId, asMap: Map[MonsterType, BasketLine]) extends Result
}

object BasketView {
  def props = Props(new BasketView)
}
class BasketView extends PersistentView {
  override def persistenceId: String = "basketSpike"
  override def viewId: String = "basketView"

  import BasketViewProtocol._

  var basket: Map[MonsterType, BasketLine] = Map.empty

  override def receive: Receive =
    queries orElse eventHandler

  def eventHandler: Receive = {
    case AddToBasket(_, monster) =>
      println("adding monster to basket in view")
      val basketLine = basket.getOrElse(
        monster.monsterType,
        BasketLine(monster.monsterType, monster.price, 0))

      basket = basket.updated(monster.monsterType, basketLine.copy(amount = basketLine.amount + 1))

    case RemoveFromBasket(_, monster) =>
      println("removing monster from basket")
      val basketLine = basket.get(monster.monsterType).filter(_.amount > 1)

      if (basketLine.isDefined) {
        val b = basketLine.get
        basket = basket.updated(monster.monsterType, b.copy(amount = b.amount - 1))
      } else {
        basket -= monster.monsterType
      }

    case msg =>
      println(s"view received: $msg")
  }

  def queries : Receive = {
    case GetSum(id) =>
      val sum = basket.values.map {
        line => line.price.asInt * line.amount
      }.sum
      sender ! Sum(id, sum)

    case GetBasket(id) =>
      println("GetBasket")
      println(basket)
      sender ! Basket(id, basket)
  }
}
