
import akka.actor.typed.{ActorRef, ActorSystem, Behavior}
import akka.actor.typed.scaladsl.Behaviors

object AddingServer{
    //Тип суммируемого значения
    type Addable = Int | Double | String

    case class SumRequest(a: Addable, b: Addable, replyTo: ActorRef[Addable])

    def apply(): Behavior[SumRequest] = Behaviors.receive { (context, message) =>
        val result = (message.a, message.b) match {
            case (a: String, b: Addable) => a + b
            case (a: Addable, b: String) => b.toString.prependedAll(a.toString())
            case (a: Int, b: Int)        => a + b
            case (a: Double, b: Double)  => a + b
            case (_: Int, _: Double) => ???
            case (_: Double, _: Int) => ???
        }
        message.replyTo ! result
        Behaviors.same
    }
}

object AddingClient{

    import AddingServer.Addable
    import AddingServer.SumRequest

    def apply(server: ActorRef[AddingServer.SumRequest]): Behavior[Addable] = Behaviors.setup { context =>
        server ! SumRequest(2, 2, context.self)
        server ! SumRequest(1.5, 1.5, context.self)
        server ! SumRequest("Hello, ", "World!", context.self)

        Behaviors.receiveMessage { result =>
            context.log.info(s"Received sum result: $result")
            Behaviors.same
        }
    }
}

object AddingSystem{
    import AddingServer.SumRequest
    
    def apply(): Behavior[SumRequest] = Behaviors.setup { context =>
        //Создаем сервер
        val server = context.spawn(AddingServer(), "server")
        //Создаем клиента
        val client = context.spawn(AddingClient(server), "client")
        Behaviors.empty
    }
}

@main def AddingMain(): Unit ={
    ActorSystem(AddingSystem(), "system")
}