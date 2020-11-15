package com.acceler8tion.geogamble.core.command

import com.acceler8tion.geogamble.api.service.Command
import com.acceler8tion.geogamble.api.service.PaginatedMessage
import com.acceler8tion.geogamble.api.service.annotation.*
import com.acceler8tion.geogamble.core.Bot
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.MessageBuilder
import net.dv8tion.jda.api.entities.Message
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent
import net.dv8tion.jda.api.events.message.priv.PrivateMessageReceivedEvent
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.TimeUnit

@Alias("help", ["page?"])
@ArgumentInfo([
        "1~10---The Page"
])
@Preview("Gets an command list by page")
@Description("No description")
@Example([
    "gg!help",
    "gg!help 2"
])
@ReceivedType(ExecuteWhen.ANYTHING)
class HelpCommand: Command {

    companion object {
        @JvmStatic
        private val LOGGER = LoggerFactory.getLogger(HelpCommand::class.java)
        private const val MAX_PAGE = 4
    }

    override fun run(bot: Bot, event: GuildMessageReceivedEvent) {
        val paginatedMessage = bot.paginatedMessageManager
                .find(event.channel.idLong, event.author.idLong)
        if(paginatedMessage == null) {
            bot
                    .reply(event.channel, "Wait for seconds...")
                    .queueAfter(1, TimeUnit.SECONDS) {
                        register(bot, event.channel.idLong, event.author.idLong, it)
                    }
        }
    }

    override fun run(bot: Bot, event: PrivateMessageReceivedEvent) {
        val paginatedMessage = bot.paginatedMessageManager
                .find(event.channel.idLong, event.author.idLong)
        if(paginatedMessage == null) {
            bot
                    .replyToDm(event.channel, "Wait for seconds...")
                    .queueAfter(1, TimeUnit.SECONDS) {
                        register(bot, event.channel.idLong, event.author.idLong, it)
                    }
        }
    }

    private fun register(bot: Bot, channelId: Long, userId: Long, message: Message) {
        val messageUpdateAction: ((PaginatedMessage) -> Message) = messageUpdateAction@{ paginatedMessage ->
            val avatarURL = paginatedMessage.impl?.get("avatarurl")
            val embed = EmbedBuilder()
                    .setAuthor("『 GeoGamble - Help 』", avatarURL)
                    .setTitle("Current-Page [$paginatedMessage.currentPage/${paginatedMessage.maxPage}]")
                    .setDescription("This is the `help` page for the **GeoGamble** bot.\n\n")
                    .appendDescription("● Use `gg?<Command-Name>` to view detailed command descriptions.\nex) `gg?about`\n")
                    .appendDescription("● Tap the emoji below to move the page")
                    .setThumbnail(avatarURL)
                    .apply {
                        val list = bot.commandManager.list()
                        list.forEach {
                            val command = it.javaClass
                            val alias = command.getAnnotation(Alias::class.java).alias
                            val args = command.getAnnotation(Alias::class.java).args
                            val preview = command.getAnnotation(Preview::class.java).preview
                            val title = if(args.isEmpty()) "gg!$alias"
                            else {
                                val a = args.joinToString(" ") { "<$it>" }
                                "gg!$alias $a"
                            }

                            addField(
                                    MessageEmbed.Field(
                                            title,
                                            "$preview\nMore info? `gg?$alias`",
                                            false
                                    )
                            )
                        }}.build()
            return@messageUpdateAction MessageBuilder()
                    .setEmbed(embed)
                    .build()
        }
        val paginatedMessage = PaginatedMessage(
                1,
                MAX_PAGE,
                message.idLong,
                messageUpdateAction,
                Duration.ofMinutes(5),
                mapOf(
                        "avatarURL" to bot.jda.selfUser.defaultAvatarUrl
                )
        )
        bot.paginatedMessageManager.register(channelId, userId, paginatedMessage, true)
        paginatedMessage.newMessageBuilder.invoke(paginatedMessage) //Need
    }

}