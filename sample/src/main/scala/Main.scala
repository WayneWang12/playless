import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpResponse
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.ExceptionHandler
import akka.stream.ActorMaterializer
import dao.CatDAO
import akka.http.scaladsl.model.StatusCodes._
import models.{Cat, CatJsonSupport}
import play.api.{Logger, Play, Playless}

import scala.io.StdIn

/**
  * @author wayne 
  * @since 17-1-9
  */
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
