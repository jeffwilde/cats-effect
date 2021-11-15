/*
 * Copyright 2020-2021 Typelevel
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package cats.effect

import cats.syntax.all._

import scala.scalajs.js
import scala.util.Try

private[effect] object process {

  def argv: Option[List[String]] = Try(
    js.Dynamic.global.process.argv.asInstanceOf[js.Array[String]].toList.drop(2)).toOption

  def env(key: String): Option[String] =
    Try(js.Dynamic.global.process.env.selectDynamic(key))
      .orElse(Try(js.Dynamic.global.process.env.selectDynamic(s"REACT_APP_$key")))
      .widen[Any]
      .collect { case v: String if !js.isUndefined(v) => v }
      .toOption

  def on(eventName: String, listener: js.Function0[Unit]): Unit =
    Try(js.Dynamic.global.process.env.on(eventName, listener).asInstanceOf[Unit]).recover {
      case _ => ()
    } // Silently ignore failure
      .get

}
