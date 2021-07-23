package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

class UnknownCommand extends Command {
  override def apply(state: State): State =
    state.setMessage("Command not found")
}
