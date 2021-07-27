package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

trait Command extends (State => State) {

}

object Command {
  val MKDIR = "mkdir"
  val LS = "ls"
  val PWD = "pwd"
  val TOUCH = "touch"
  val CD = "cd"
  val RM = "rm"
  val ECHO = "echo"
  val CAT = "cat"

  def from(input: String): Command = {
    val token: Array[String] = input.split(" ")

    if (input.isEmpty || token.isEmpty) emptyCommand
    else token(0) match {
      case MKDIR =>
        if (token.length < 2) incompleteCommand(MKDIR)
        else new Mkdir(token(1))
      case LS =>
        new Ls
      case PWD =>
        new Pwd
      case TOUCH =>
        if (token.length < 2) incompleteCommand(MKDIR)
        else new Touch(token(1))
      case CD =>
        if (token.length < 2) incompleteCommand(CD)
        else new Cd(token(1))
      case RM =>
        if (token.length < 2) incompleteCommand(RM)
        else new Rm(token(1))
      case ECHO =>
        if (token.length < 2) incompleteCommand(ECHO)
        else new Echo(token.tail.toList)
      case CAT =>
        if (token.length < 2) incompleteCommand(CAT)
        else new Cat(token(1))
      case _ =>
        new UnknownCommand
    }
  }

  def incompleteCommand(name: String): Command = (state: State) => {
    state.setMessage(name + " incomplete command")
  }

  def emptyCommand: Command = (state: State) => state
}