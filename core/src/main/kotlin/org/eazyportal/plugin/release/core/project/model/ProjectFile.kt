package org.eazyportal.plugin.release.core.project.model

import java.io.File
import java.io.IOException

interface ProjectFile<T> {

    @Throws(IOException::class, InterruptedException::class)
    fun createIfMissing()

    @Throws(IOException::class, InterruptedException::class)
    fun exists(): Boolean

    fun getFile(): T

    @Throws(IOException::class, InterruptedException::class)
    fun isDirectory(): Boolean

    @Throws(IOException::class)
    fun isFile(): Boolean

    @Throws(IOException::class, InterruptedException::class)
    fun readLines(): List<String>

    @Throws(IOException::class, InterruptedException::class)
    fun readText(): String

    fun resolve(subPath: String): ProjectFile<T>

    @Throws(IOException::class, InterruptedException::class)
    fun writeText(content: String)

}

data class FileSystemProjectFile(
    private val file: File
) : ProjectFile<File> {

    override fun createIfMissing() {
        file.createNewFile()
    }

    override fun exists(): Boolean =
        file.exists()

    override fun getFile(): File =
        file

    override fun isDirectory(): Boolean =
        file.isDirectory

    override fun isFile(): Boolean =
        file.isFile

    override fun readLines(): List<String> =
        file.readLines()

    override fun readText(): String =
        file.readText()

    override fun resolve(subPath: String): FileSystemProjectFile =
        FileSystemProjectFile(file.resolve(subPath))

    override fun writeText(content: String) {
        file.writeText(content)
    }

    override fun toString(): String {
        return file.path
    }

}
