package tutorial.webapp.services.js

import scala.scalajs.js

/* Wrapper error around javascript native exceptions */
final class JsError private (e: js.JavaScriptException) {
  val message = e.getMessage()
}

object JsError {
  def apply(e: js.JavaScriptException) = new JsError(e)
}

