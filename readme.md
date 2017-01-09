# Playless

Playless extract the injector from play framework to make it independent of play's netty server or akka http server, which makes you harness the power of reusing code intended to play framework while you are using a new server backend which can be anything.
 
All you have to do is just a piece of code as below, which I take akka http and play slick for an example:

```
object Main extends App with CatJsonSupport {

  val injector = Playless.startPlayAndGetInjector(args)
  implicit val system = injector.instanceOf[ActorSystem]
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  val catDao = injector.instanceOf[CatDAO]

  val defaultExceptionHandler = ExceptionHandler {
    case t: Throwable =>
      extractLog { logger =>
        logger.error(t, "error happened!")
        complete(HttpResponse(InternalServerError, entity = t.getMessage))
      }
  }

  val route = handleExceptions(defaultExceptionHandler){
    path("cats") {
      get {
        complete(catDao.all())
      } ~ post {
        entity(as[Cat]) { cat =>
          complete(catDao.insert(cat))
        }
      }
    }
  }

  val bindingFuture = Http().bindAndHandle(route, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => Play.current.stop()) // and shutdown when done

}
```

See the complete code in `sample` project!