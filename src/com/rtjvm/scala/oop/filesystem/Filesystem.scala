package com.rtjvm.scala.oop.filesystem

import com.rtjvm.scala.oop.commads.Command
import com.rtjvm.scala.oop.files.Directory

import java.util.Scanner

object Filesystem extends App {

  val root = Directory.ROOT
  // [1, 2, 3, 4]
  /*
    0 (op) 1 => 1
    1 (op) 2 => 3
}
    3 (op) 3 => 6
    6 (op) 4 => 10

    List(1,2,3,4).foldLeft(0)(x+y) => x+y
   */
  io.Source.stdin.getLines().foldLeft(State(root, root))((currentState, newLine) => {
    currentState.show
    val newState = Command.from(newLine).apply(currentState)
    newState
  })
}

