package com.acceler8tion.geogamble.core

import com.acceler8tion.geogamble.api.service.Command
import com.acceler8tion.geogamble.api.service.CommandManager
import com.acceler8tion.geogamble.api.service.PaginatedMessageManager
import com.acceler8tion.geogamble.core.command.HelpCommand
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.PrivateChannel
import net.dv8tion.jda.api.entities.TextChannel
import net.dv8tion.jda.api.requests.restaction.MessageAction

class Bot(val jda: JDA) {

    companion object {
        const val name = "GeoGamble"
        lateinit var selfId: String
    }

    val commandManager = CommandManager(
            listOf<Command>(
                    HelpCommand()
            )
    )
    val paginatedMessageManager = PaginatedMessageManager(jda)

    fun reply(channel: TextChannel, text: String, vararg args: String?): MessageAction {
        return channel
                .sendMessageFormat(text, args)
    }

    fun reply(channel: TextChannel, embed: MessageEmbed): MessageAction {
        return channel
                .sendMessage(embed)
    }

    fun reply(channel: TextChannel, message: Message): MessageAction {
        return channel
                .sendMessage(message)
    }

    fun replyToDm(channel: PrivateChannel, text: String, vararg args: String?): MessageAction {
        return channel
                .sendMessageFormat(text, args)
    }

    fun replyToDm(channel: PrivateChannel, embed: MessageEmbed): MessageAction {
        return channel
                .sendMessage(embed)
    }

    fun replyToDm(channel: PrivateChannel, message: Message): MessageAction {
        return channel
                .sendMessage(message)
    }
}