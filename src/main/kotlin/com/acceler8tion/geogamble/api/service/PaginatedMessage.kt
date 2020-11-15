package com.acceler8tion.geogamble.api.service

import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.requests.restaction.MessageAction
import java.time.Duration

data class PaginatedMessage(
        var currentPage: Int,
        val maxPage: Int,
        val messageId: Long,
        val newMessageBuilder: (PaginatedMessage) -> Message,
        val ed: Duration,
        val impl: Map<String, String>?
) {
    val expirationDuration: Long = System.currentTimeMillis() + ed.toMillis()
}