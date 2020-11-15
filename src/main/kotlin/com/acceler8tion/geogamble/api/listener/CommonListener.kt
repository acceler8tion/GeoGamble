package com.acceler8tion.geogamble.api.listener

import net.dv8tion.jda.api.events.GenericEvent
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter

class CommonListener : ListenerAdapter() {

    override fun onGenericEvent(event: GenericEvent) {
        when(event) {
            is GuildMessageReceivedEvent -> onGuildMessageReceived(event)
        }
    }

    override fun onGuildMessageReceived(event: GuildMessageReceivedEvent) {
        super.onGuildMessageReceived(event)
    }
}