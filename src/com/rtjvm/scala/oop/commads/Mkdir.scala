package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

class Mkdir(name: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
    /* Example 1:
      someDir
        /a
        /b
        (new) /d
      =>
       new someDir
        /a
        /b
        /d
     */
    /* Example 2:
      /a/b
        /c
        /d
        /new /e

        =>
      new /a
        create a new b folder
          /c
          /d
          /e
     */
    /* Execution:
      /a/b
        (contents)
        (new entry) /e

        newRoot = updateStructure(root, ["a, "b"], /e) = root.replaceEntry("a", updateStructure(/a, ["b"], /e) = /a.replaceEntry("/b", updateStructure(/b, [], /e) = /b.add(/e)))
          => path.isEmpty?
          => oldEntry = /a
          root.replaceEntry("a", updateStructure(/a,["b"], /e) = /a.replaceEntry("/b", updateStructure(/b, [], /e) = /b.add(/e)))
            =>path.isEmpty
            =>oldEntry=/b
            /a.replaceEntry("/b", updateStructure(/b, [], /e) = /b.add(/e))
              => path.isEmpty? -> b.add(/e)
       */
    if (path.isEmpty) currentDirectory.addEntry(newEntry)
    else {
      // currentDirectory is /a
      // path = ["b"]
      println(path)
      println(path.head)
      println(path.head.isEmpty)
      println(currentDirectory.findEntry(path.head))
      val oldEntry = currentDirectory.findEntry(path.head).asDirectory
      currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
    }

  }

  def doMkdir(state: State, name: String): State = {
    val wd = state.workingDirectory

    // 1. get all the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. create new directory entry in the working directory
    val newDirectory = Directory.empty(wd.path, name)

    // 3. update the whole directory structure starting form the root
    // DIRECTORY STRUCTURE IS IMMUTABLE
    val newRoot = updateStructure(state.root, allDirsInPath, newDirectory)

    // 4. find the new wd instance given working directory's full path, in the new directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)
    State(newRoot, newWd)
  }

  def apply(state: State): State = {
    val wd = state.workingDirectory
    if (wd.hasEntry(name)) {
      state.setMessage("Entry " + name + " already exists!")
    } else if (name.contains(Directory.SEPARATOR)) {
      state.setMessage(name + " must not contain separators!")

    } else if (checkIllegal(name)) {
      state.setMessage(name + ": illegal entry name")
    } else {
      doMkdir(state, name)
    }
  }
}
