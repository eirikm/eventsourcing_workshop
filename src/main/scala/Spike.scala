import akka.actor.Props
import akka.persistence.PersistentActor

/**
 * Created by jarnyste on 09/09/14.
 */

sealed trait Command
sealed trait Event
case class AddMonsterToBasket(basketId: BasketId ,monster: Monster) extends Command
case class RemoveMonsterFromBasket(basketId:BasketId, monster:Monster) extends Command

case class AddToBasket(basketId:BasketId, monster : Monster) extends Event
case class RemoveFromBasket(basketId:BasketId,monster:Monster) extends Event

object Spike{
  def props:Props= {
    Props(new Spike)
  }
}


class Spike extends PersistentActor {

  var baskets = Map.empty[BasketId, Basket]

  def updateState(evt: Event): Unit = {
    evt match {
      case AddToBasket(basketId, basketLine) => println(s"basket update $basketId $basketLine")
    }
  }

  override def receiveRecover: Receive = {
    case foo =>
  }

  override def receiveCommand: Receive = {
    case AddMonsterToBasket(id, monster) =>
      persist(AddToBasket(id, monster))(updateState)
  }

  override def persistenceId: String = "basketSpike"
}
