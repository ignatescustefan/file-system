package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

import scala.annotation.tailrec

class Cd(dirName: String) extends Command {

  def doFindEntry(root: Directory, absolutePath: String): DirEntry = {
    @tailrec
    def findEntryHelper(currentDirectory: Directory, path: List[String]): DirEntry = {
      if (path.isEmpty || path.head.isEmpty) currentDirectory
      else if (path.tail.isEmpty) currentDirectory.findEntry(path.head)
      else {
        val nextDir = currentDirectory.findEntry(path.head)
        if (nextDir == null || !nextDir.isDirectory) null
        else findEntryHelper(nextDir.asDirectory, path.tail)
      }
    }

    // 1. tokens
    val tokens: List[String] = absolutePath.substring(1).split(Directory.SEPARATOR).toList
    // 2. navigate to the correct entry
    findEntryHelper(root, tokens)
  }

  override def apply(state: State): State = {
    /*
     cd /something/smtElse/.../
     cd a/b/c
     cd ..
     cd .
     cd a/./.././a/
     */
    // Steps:
    /*
    1. find root
    2. find the absolute path of the directory
    3. find the directory to cd to, given path
    4. change the state given the new directory

     */
    // 1
    val root = state.root
    val wd = state.workingDirectory

    // 2
    val absolutePath = {
      if (dirName.startsWith(Directory.SEPARATOR)) dirName
      else if (wd.isRoot) wd.path + dirName
      else wd.path + Directory.SEPARATOR + dirName
    }
    // 3
    val destinationDirectory = doFindEntry(root, absolutePath)

    // 4
    if (destinationDirectory == null || !destinationDirectory.isDirectory) {
      state.setMessage(dirName + ": no such directory.")
    } else State(root, destinationDirectory.asDirectory, "")
  }
}
