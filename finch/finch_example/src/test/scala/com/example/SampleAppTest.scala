package com.example

import scala.util.{Try, Success, Failure}

import org.scalatest._

import io.finch._
import com.twitter.io.Buf

class SampleAppTest extends FlatSpec with Matchers {
  import UserAPI._

  behavior of "the user endpoints"

  it should "list users" in {
    val req = Input.get("/")
    getUsers(req).awaitValueUnsafe() should be (Some(Seq(User(1, "initial_user"))))
  }

  it should "add user" in {
    val req = Input.post("/").withBody[Application.Json](
      Buf.Utf8("""{"id": 2, "name": "new"}""")
    )
    newUser(req).awaitValueUnsafe() should be (Some(User(2, "new")))
  }

  it should "delete user" in {
    val req = Input.delete("/2")
    delUser(req).awaitValueUnsafe() should be (Some(User(2, "new")))
  }

  it should "return exception" in {
    val req = Input.delete("/0")
    Try(delUser(req).awaitValueUnsafe()) match {
      case Failure(_: Error.NotValid) => true
      case _ => fail("NotValid should be returned")
    }
  }
}
