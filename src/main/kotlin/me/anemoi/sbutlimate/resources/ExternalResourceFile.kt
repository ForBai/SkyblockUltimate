package me.anemoi.sbutlimate.resources

import floppaclient.FloppaClient
import java.io.File
import java.io.FileOutputStream
import java.net.URL
import java.nio.channels.Channels

object ExternalResourceFile {
    /*
    val baseDirectory = File(FloppaClient.mc.mcDataDir, "config/${FloppaClient.CONFIG_DOMAIN}/resources")
    val resourceFiles = mutableListOf<ResourceFile>()

    fun addResourceFile(resourceFile: ResourceFile) {
        resourceFiles.add(resourceFile)
        downloadFile(resourceFile.downloadFrom, baseDirectory.absolutePath)
    }

    fun addResource(name: String, downloadFrom: String, isDir: Boolean) {
        addResourceFile(ResourceFile(name, downloadFrom, baseDirectory.absolutePath, isDir))
    }

    fun addResource(name: String, downloadFrom: String, isDir: Boolean, fileLocation: String) {
        addResourceFile(ResourceFile(name, downloadFrom, fileLocation, isDir))
    }

    fun addResource(downloadFrom: String) {
        val name = downloadFrom.substringAfterLast("/")
        addResourceFile(ResourceFile(name, downloadFrom, baseDirectory.absolutePath, false))
    }

    fun addResource(name: String, downloadFrom: String) {
        addResourceFile(ResourceFile(name, downloadFrom, baseDirectory.absolutePath, false))
    }

    fun getResourceFile(name: String): ResourceFile? {
        return resourceFiles.find { it.name == name }
    }

    fun getResourceFile(name: String, fileLocation: String): ResourceFile? {
        return resourceFiles.find { it.name == name && it.fileLocation == fileLocation }
    }

    fun loadResources() {
        //the base directory should be in floppaclient directory
        if (!baseDirectory.exists()) {
            baseDirectory.mkdirs()
        }

        //delete all files in the base directory and then redownload them
        baseDirectory.listFiles()?.forEach { it.delete() }
        resourceFiles.forEach { downloadFile(it.downloadFrom, it.fileLocation) }
    }


    //download file from url to location
    fun downloadFile(url: URL, location: String) {
        val rbc = Channels.newChannel(url.openStream())
        val fos = FileOutputStream(location)
        fos.channel.transferFrom(rbc, 0, Long.MAX_VALUE)
    }

    fun downloadFile(url: String, location: String) {
        downloadFile(URL(url), location)
    }

     */
}