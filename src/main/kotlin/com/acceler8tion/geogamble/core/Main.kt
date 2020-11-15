package com.acceler8tion.geogamble.core

import com.acceler8tion.geogamble.api.listener.CommonListener
import com.acceler8tion.geogamble.api.util.AttributeReader
import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.OnlineStatus
import net.dv8tion.jda.api.entities.Activity
import net.dv8tion.jda.api.utils.cache.CacheFlag

class Main {

    companion object {
        private const val BOT_PROPS = "conf/bot.properties"

        @JvmStatic
        fun connect() {
            val botConfig = AttributeReader(BOT_PROPS)

            val token = botConfig.get("token")
            val clientId = botConfig.get("client_id")
            val status = botConfig.get("status")
            val activity = botConfig.get("activity")

            val builder = JDABuilder.createDefault(token)
            val jda = builder.apply {
                disableCache(
                    CacheFlag.ACTIVITY,
                    CacheFlag.VOICE_STATE
                )
                    .setBulkDeleteSplittingEnabled(false)
                    .setStatus(
                        when(status) {
                            "online" -> OnlineStatus.ONLINE
                            "idle" -> OnlineStatus.IDLE
                            "doNotDisturb" -> OnlineStatus.DO_NOT_DISTURB
                            "invisible" -> OnlineStatus.INVISIBLE
                            "offline" -> OnlineStatus.OFFLINE

                            else -> OnlineStatus.UNKNOWN
                        }
                    )
                    .setActivity(
                        parseActivity(activity
                                        .split(" / "))
                    )
                        .addEventListeners(
                                CommonListener()
                        )

                Bot.selfId = clientId
            }
                .build()

            jda.awaitReady() //Blocking
        }

        @JvmStatic
        fun parseActivity(activityInfo: List<String>): Activity {
            val type = activityInfo[0]
            val desc = activityInfo[1]
            val url = activityInfo[2]

            return when(type) {
                "Playing" -> Activity.playing(desc)
                "Streaming" -> Activity.streaming(desc, url)
                "Watching" -> Activity.watching(desc)
                "Listening" -> Activity.listening(desc)

                    else -> Activity.playing("nothing..")
            }
        }
    }
}