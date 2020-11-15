package com.acceler8tion.geogamble.api.util

import org.slf4j.LoggerFactory
import java.io.FileReader
import java.io.IOException
import java.util.*

class AttributeReader(private val route: String) {

    companion object {
        private val LOGGER = LoggerFactory.getLogger(AttributeReader::class.java)
    }
    private val attr = Properties()

    init {
        load()
    }

    private fun load() {
        val reader = FileReader(route)
        try {
            attr.load(reader)
        } catch (e: IOException) {
            val spl = route.split("/")
            val log = "Failed to read `${spl[spl.size]}`"
            LOGGER.warn(log, e)
        }
    }

    fun get(key: String): String {
        return attr.getProperty(key)
    }

    fun getOrElse(key: String, def: String): String {
        return attr.getProperty(key, def)
    }
}