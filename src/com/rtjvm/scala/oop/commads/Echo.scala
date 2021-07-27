package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.files.{Directory, File}
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

  def getRootAfterEcho(currentDirectory: Directory, path: List[String], contents: String, append: Boolean): Directory = {
    /*
      if path empty, then fail (currentDirectory)
      else if no more things to explore = path.tail.isEmpty
        find the file to create/add content to
        if file not found, create file
        else if the entry is actually a directory, then fail
        else
          replace or append content to the file
          replace the entry with the filename with the new file
       else
        find the next directory to navigate
        call getRootAfterEcho recursively on that

        if recursive call failed, fail
        else replace entry with the new directory after the recursive call
     */

    if (path.isEmpty) currentDirectory
    else if (path.tail.isEmpty) {
      val dirEntry = currentDirectory.findEntry(path.head)

      if (dirEntry == null)
        currentDirectory.addEntry(
          new File(currentDirectory.path, path.head, contents))
      else if (dirEntry.isDirectory) currentDirectory
      else if (append) currentDirectory.replaceEntry(path.head, dirEntry.asFile.appendContents(contents))
      else currentDirectory.replaceEntry(path.head, dirEntry.asFile.setContent(contents))
    } else {
      val nextDirectory = currentDirectory.findEntry(path.head).asDirectory
      val newNextDirectory = getRootAfterEcho(nextDirectory, path.tail, contents, append)
      if (newNextDirectory == nextDirectory) currentDirectory
      else currentDirectory.replaceEntry(path.head, newNextDirectory)
    }

  }

  def doEcho(state: State, content: String, fileName: String, append: Boolean): State = {
    if (fileName.contains(Directory.SEPARATOR))
      state.setMessage("Echo: filename must not contain separators")
    else {
      val newRoot: Directory = getRootAfterEcho(state.root, state.workingDirectory.getAllFoldersInPath :+ fileName, content, append)
      if (newRoot == state.root)
        state.setMessage(fileName + ": no such file")
      else
        State(newRoot, newRoot.findDescendant(state.workingDirectory.getAllFoldersInPath))
    }
  }
}
