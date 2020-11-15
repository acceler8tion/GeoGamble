package com.acceler8tion.geogamble.api.service

import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.util.concurrent.ConcurrentHashMap

class PaginatedMessageManager(private val jda: JDA) {

    companion object {
        @JvmStatic
        private val provider = ConcurrentHashMap<String, PaginatedMessage>()
        private fun encKey(channelId: Long, userId: Long) = "$channelId::$userId"
    }

    fun find(channelId: Long, userId: Long): Updater? {
        val key = encKey(channelId, userId)
        val pm = provider[key]
        return if(pm == null) null
        else {
            if(pm.expirationDuration > System.currentTimeMillis()){
                unregister(channelId, userId)
                null
            } else Updater(channelId, pm)
        }
    }

    fun register(channelId: Long, userId: Long, pm: PaginatedMessage, replaceIfExist: Boolean) {
        val key = encKey(channelId, userId)
        if(replaceIfExist) provider.replace(key, pm)
        else provider.putIfAbsent(key, pm)
    }

    private fun unregister(channelId: Long, userId: Long) {
        val key = encKey(channelId, userId)
        provider.remove(key)
    }

    inner class Updater(
        private val channelId: Long, 
        private val pm: PaginatedMessage
    ) {

        fun next(allowToGoFirstPage: Boolean): Updater {
            if(pm.currentPage != pm.maxPage) pm.currentPage++
            else {
                if(allowToGoFirstPage) pm.currentPage = 1
            }
            return this
        }

        fun pre(allowToGoLastPage: Boolean): Updater {
            if(pm.currentPage != 1) pm.currentPage--
            else {
                if(allowToGoLastPage) pm.currentPage = pm.maxPage
            }
            return this
        }

        fun update(): MessageAction? {
            return jda.getTextChannelById(channelId)?.editMessageById(pm.messageId, pm.newMessageBuilder.invoke(pm))
        }
    }

}