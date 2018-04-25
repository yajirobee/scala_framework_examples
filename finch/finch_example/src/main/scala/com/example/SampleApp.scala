package com.example

import scala.collection.mutable.Map

import com.twitter.finagle.Http
import com.twitter.util.Await

import io.finch._
import io.finch.circe._
import io.finch.syntax._
import io.circe.generic.auto._

case class User(id: Int, name: String)
case class Users(users: Map[Int, User])

object UserAPI {
  val users = Users(Map(1 -> User(1, "initial_user")))

  val getUsers = get(/) { () =>
    Ok(users.users.values.toSeq)
  }

  val newUser = post(jsonBody[User]) { u: User =>
    users.users += u.id -> u
    Ok(u)
  }

  val delUser = delete(
    path[Int].should("be positive") { _ > 0 }
  ) { id: Int =>
    users.users.remove(id)
      .map { user => Ok(user) }
      .getOrElse { NotFound(new Exception("not found")) }
  }

  val usersApi = "users" :: (getUsers :+: newUser :+: delUser)
}

object SampleApp extends App {

  val api = "v1" :: UserAPI.usersApi

  Await.ready(Http.server.serve(":8080", api.toService))
}
