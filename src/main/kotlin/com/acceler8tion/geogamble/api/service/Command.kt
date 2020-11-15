package com.acceler8tion.geogamble.api.service

import com.acceler8tion.geogamble.Bot
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import kotlin.coroutines.CoroutineContext

interface Command: CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = SupervisorJob() + Dispatchers.Main

    /**
     * Run the command if message received from Guild.
     *
     * @param bot The Bot
     * @param event MessageReceivedEvent from guild
     */
    fun run(bot: Bot, event: GuildMessageReceivedEvent) {
        //Override optional
    }

    /**
     * Run the command if message received from Direct Message.
     *
     * @param bot The Bot
     * @param event MessageReceivedEvent from direct message
     */
    fun run(bot: Bot, event: PrivateMessageReceivedEvent) {
        //Override optional
    }

}