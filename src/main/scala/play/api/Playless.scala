package play.api

import play.api.inject.Injector
import play.core.server.{RealServerProcess, ServerConfig}

import scala.util.control.NonFatal

/**
  * @author wayne 
  * @since 17-1-9
  */
object Playless {

  def startPlayAndGetInjector(args:Seq[String] = Nil):Injector = {
    val process = new RealServerProcess(args)
    import play.core.server.ProdServerStart._
    try {

      // Read settings
      val config: ServerConfig = readServerConfigSettings(process)

      // Start the application
      val application: Application = {
        val environment = Environment(config.rootDir, process.classLoader, Mode.Prod)
        val context = ApplicationLoader.createContext(environment)
        val loader = ApplicationLoader(context)
        loader.load(context)
      }
      Play.start(application)

      // return the injector
      application.injector
    } catch {
      case NonFatal(e) =>
        process.exit("Oops, cannot start the server.", cause = Some(e))
    }
  }

}


