package com.neutron

import com.neutron.controllers.Controller
import java.io.*

internal object Writer {
    private val workDir = Controller.workDir
    fun bufferedWriter(text: String, path: String) {
        try {
            val fos = FileOutputStream(this::class.java.getResource(path)!!.file)
            val bout = BufferedOutputStream(fos)
            val a = text.toByteArray()
            bout.write(a)
            bout.flush()
            bout.close()
        } catch (ex: IOException) {
            System.err.println("Error in bufferedWriter")
            ex.printStackTrace()
        }
    }

    fun withoutOverwrite(text: String?, path: String) {
        val log = File(this::class.java.getResource(path)!!.file)
        try {
            if (!log.exists()) {
                println("We had to make a new file.")
                log.createNewFile()
            }
            val fileWriter = FileWriter(log, true)
            val bufferedWriter = BufferedWriter(fileWriter)
            bufferedWriter.write(text)
            bufferedWriter.close()
            println("Done")
        } catch (e: IOException) {
            println("COULD NOT LOG!!")
        }
    }
}