package com.acceler8tion.geogamble.api.service

import com.acceler8tion.geogamble.api.service.annotation.Alias

class CommandManager(private val list: List<Command>) {

    /**
     * Gets a list of command
     *
     * @return list of command
     */
    fun list(): List<Command> {
        return list
    }

    /**
     * Finds a command by alias
     *
     * @param alias The alias
     * @return A command matching alias
     */
    fun findByAlias(alias: String): Command? {
        return list.find {
            it.javaClass
                .getAnnotation(Alias::class.java)
                        .alias
                            .split("::")[0] == alias
                }
    }
}