package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

trait Command {
  //transform the state in a new state
  def apply(state: State): State
}

object Command {
  def from(input: String): Command =
    new UnknownCommand
}