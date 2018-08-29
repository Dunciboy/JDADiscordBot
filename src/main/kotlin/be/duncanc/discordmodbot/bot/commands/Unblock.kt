/*
 * MIT License
 *
 * Copyright (c) 2017 Duncan Casteleyn
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package be.duncanc.discordmodbot.bot.commands

import be.duncanc.discordmodbot.data.configs.properties.DiscordModBotConfigurationProperties
import be.duncanc.discordmodbot.data.repositories.BlockedUserRepository
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.springframework.stereotype.Component

@Component
class Unblock(
        val blockedUserRepository: BlockedUserRepository,
        val discordModBotConfigurationProperties: DiscordModBotConfigurationProperties
) : CommandModule(
        arrayOf("Unblock"),
        null,
        null
) {
    override fun commandExec(event: MessageReceivedEvent, command: String, arguments: String?) {
        if(event.author.idLong == discordModBotConfigurationProperties.ownerId) {
            blockedUserRepository.deleteById(event.message.contentRaw.toLong())
        }
    }
}