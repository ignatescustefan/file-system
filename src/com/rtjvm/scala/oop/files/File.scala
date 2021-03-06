package com.rtjvm.scala.oop.files

import com.rtjvm.scala.oop.filesystem.FilesystemException

class File(override val parentPath: String,
           override val name: String,
           val content: String)
  extends DirEntry(parentPath, name) {

  def setContent(newContents: String): File =
    new File(parentPath, name, newContents)

  def appendContents(newContents: String): File =
    setContent(content + "\n" + newContents)

  override def asDirectory: Directory =
    throw new FilesystemException("A file cannot be converted to a directory")

  override def getType: String = "File"

  def asFile: File = this

  def isFile: Boolean = true

  def isDirectory: Boolean = false
}

object File {

  def empty(parentPath: String, name: String): File =
    new File(parentPath, name, "")
}