package com.rtjvm.scala.oop.files

class Directory(override val parentPath: String,
                override val name: String,
                val contents: List[DirEntry]
               ) extends DirEntry(parentPath, name) {
  def replaceEntry(entryName: String, newEntry: DirEntry): Directory = ???

  def findEntry(entryName: String): DirEntry = ???

  def addEntry(newEntry: DirEntry): Directory = ???

  def findDescendant(allDirsInPath: List[String]): Directory = ???

  def hasEntry(name: String): Boolean = ???

  def getAllFoldersInPath: List[String] = {
    // /a/b/c/d => List("a", "b",.."d")
    path.substring(1).split(Directory.SEPARATOR).toList
  }

  def asDirectory: Directory = this
}

object Directory {
  val SEPARATOR = "/"
  val ROOT_PATH = "/"

  def ROOT: Directory = Directory.empty("", "")

  def empty(path: String, name: String): Directory =
    new Directory(path, name, List())
}