package models

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, RootJsonFormat}

/**
  * @author wayne 
  * @since 17-1-9
  */

case class Cat(name: String, color: String)

trait CatJsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val catFormatter: RootJsonFormat[Cat] = jsonFormat2(Cat)
}

