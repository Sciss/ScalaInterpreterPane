/*
 *  InterpreterImpl.scala
 *  (ScalaInterpreterPane)
 *
 *  Copyright (c) 2010-2020 Hanns Holger Rutz. All rights reserved.
 *
 *  This software is published under the GNU Lesser General Public License v2.1+
 *
 *  For further information, please contact Hanns Holger Rutz at
 *  contact@sciss.de
 */

package de.sciss.scalainterpreter
package impl

import java.io.Writer

import scala.collection.immutable.{Seq => ISeq}

object InterpreterImpl {
  import Interpreter.{Config, ConfigBuilder}

  def apply(config: Config): Interpreter = {
    val in = makeIMain(config)
    new Impl(in)
  }

  def newConfigBuilder(): ConfigBuilder = new ConfigBuilderImpl

  def mkConfigBuilder(config: Config): ConfigBuilder = {
    import config._
    val b           = new ConfigBuilderImpl
    b.imports       = imports
    b.bindings      = bindings
    b.executor      = executor
    b.out           = out
    b.quietImports  = quietImports
    b
  }

  private final class ConfigBuilderImpl extends ConfigBuilder {
    var imports     : ISeq[String]         = Nil
    var bindings    : ISeq[(String, Any)]  = Nil
    var executor      = ""
    var out           = Option.empty[Writer]
    var quietImports  = true

    def build: Config = ConfigImpl(
      imports = imports, bindings = bindings, executor = executor, out = out, quietImports = quietImports)

    override def toString = s"Interpreter.ConfigBuilder@${hashCode().toHexString}"
  }

  private final case class ConfigImpl(imports: ISeq[String], bindings: ISeq[(String, Any)],
                                      executor: String, out: Option[Writer], quietImports: Boolean)
    extends Config {

    override def toString = s"Interpreter.Config@${hashCode().toHexString}"
  }


  private def makeIMain(config: Config): IntpInterface = {
    val in: IntpInterface = MakeIMain(config)

    config.bindings.foreach(in.bind)
    if (config.imports.nonEmpty) {
      in.interpretWithoutResult(config.imports.mkString("import ", ", ", ""), quiet = config.quietImports)
    }
    in.setExecutionWrapper(config.executor)
    in
  }

  private final class Impl(in: IntpInterface) extends Interpreter {
    private lazy val cmp: Completer = in.mkCompleter() // new ScalaCompleterImpl(in)

    override def toString = s"Interpreter@${hashCode().toHexString}"

    def completer: Completer = cmp

    def interpretWithResult(code: String, quiet: Boolean): Interpreter.Result =
      in.interpretWithResult(code, quiet = quiet)

    def interpret(code: String, quiet: Boolean): Interpreter.Result =
      in.interpretWithoutResult(code, quiet = quiet)
  }
}