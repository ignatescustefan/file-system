package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.filesystem.State

import scala.annotation.tailrec
import scala.jdk.Accumulator

class Echo(args: List[String]) extends Command {
  override def apply(state: State): State = {
    /*
    if no args, state
    else if juste one arg, print to console
    else if multiple args
    {
      operator = next to last argument
      if >
        echo to a file (may create a file if not there
      if >>
        append to a file
      else
        just echo everything to console
    }
     */
    if (args.isEmpty) state
    else if (args.length == 1) state.setMessage(args(0))
    else {
      val operator = args(args.length - 2)
      val filename = args.last
      val content = createContent(args, args.length - 2)

      if (">>".equals(operator))
        doEcho(state, content, filename, append = true)
      else if (">".equals(operator))
        doEcho(state, content, filename, append = false)
      else
        state.setMessage(createContent(args, args.length))
    }
  }

  def createContent(args: List[String], topIndex: Int): String = {
    @tailrec
    def createContentHelper(currentIndex: Int, accumulator: String): String = {
      if (currentIndex >= topIndex) accumulator
      else createContentHelper(currentIndex + 1, accumulator + " " + args(currentIndex))
    }

    createContentHelper(0, "")
  }

  def doEcho(state: State, content: String, fileName: String, append: Boolean): State = ???
}
