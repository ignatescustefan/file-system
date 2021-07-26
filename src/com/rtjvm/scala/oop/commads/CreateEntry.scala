package com.rtjvm.scala.oop.commads

import com.rtjvm.scala.oop.files.{DirEntry, Directory}
import com.rtjvm.scala.oop.filesystem.State

abstract class CreateEntry(entryName: String) extends Command {

  def checkIllegal(name: String): Boolean = {
    name.contains(".")
  }

  def updateStructure(currentDirectory: Directory, path: List[String], newEntry: DirEntry): Directory = {
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
      //      println(path)
      //      println(path.head)
      //      println(path.head.isEmpty)
      //      println(currentDirectory.findEntry(path.head))
      val oldEntry = currentDirectory.findEntry(path.head).asDirectory
      currentDirectory.replaceEntry(oldEntry.name, updateStructure(oldEntry, path.tail, newEntry))
    }

  }

  def doCreateEntry(state: State, name: String): State = {
    val wd = state.workingDirectory

    // 1. get all the directories in the full path
    val allDirsInPath = wd.getAllFoldersInPath

    // 2. create new directory entry in the working directory

    val newEntry: DirEntry = doCreateSpecificEntry(state)

    // 3. update the whole directory structure starting form the root
    // DIRECTORY STRUCTURE IS IMMUTABLE
    val newRoot = updateStructure(state.root, allDirsInPath, newEntry)

    // 4. find the new wd instance given working directory's full path, in the new directory structure
    val newWd = newRoot.findDescendant(allDirsInPath)
    State(newRoot, newWd)
  }

  def doCreateSpecificEntry(state: State): DirEntry

  def apply(state: State): State = {
    val wd = state.workingDirectory
    if (wd.hasEntry(entryName)) {
      state.setMessage("Entry " + entryName + " already exists!")
    } else if (entryName.contains(Directory.SEPARATOR)) {
      state.setMessage(entryName + " must not contain separators!")

    } else if (checkIllegal(entryName)) {
      state.setMessage(entryName + ": illegal entry name")
    } else {
      doCreateEntry(state, entryName)
    }
  }
}
