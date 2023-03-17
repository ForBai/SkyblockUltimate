package me.anemoi.sbutlimate.resources

import java.io.File
import java.io.InputStream

data class ResourceFile(val name: String, val downloadFrom: String, var fileLocation: String, val isDir: Boolean) {
    fun getFile(): File {
        val file = File(fileLocation, name)
        if (file == null || !file.exists()) {
            throw Exception("File $name does not exist, error! Try restarting the client.")
        } else {
            return File(fileLocation, name)
        }
    }

    fun getInputStream(): InputStream {
        return getFile().inputStream()
    }
}


