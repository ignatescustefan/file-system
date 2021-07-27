package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

class Cat(filename: String) extends Command {
  def apply(state: State): State = {
    val wd = state.workingDirectory

    val dirEntry = wd.findEntry(filename)
    if (dirEntry == null || !dirEntry.isFile)
      state.setMessage(filename + ": no such file")
    else
      state.setMessage(dirEntry.asFile.content)
  }
}
