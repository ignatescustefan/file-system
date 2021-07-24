package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

class Pwd extends Command {
  override def apply(state: State): State = {
    state.setMessage(state.workingDirectory.path)
  }
}
