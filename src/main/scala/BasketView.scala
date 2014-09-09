import akka.persistence.PersistentView

class BasketView extends PersistentView {
  override def persistenceId: String = "basketSpike"
  override def viewId: String = "basketView"

  var basket: Map[MonsterType, BasketLine] = Map.empty

  override def receive: Receive = {
    case AddMonsterToBasket(_, monster) =>
      println("adding monster to basket in view")
      val basketLine = basket.getOrElse(
          monster.monsterType,
          BasketLine(monster.monsterType, monster.price, 0))

      basket = basket.updated(monster.monsterType, basketLine.copy(amount = basketLine.amount + 1))

    case msg =>
      println(s"view received: $msg")
  }
}
