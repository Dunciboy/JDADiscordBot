/*
 * Copyright 2018 Duncan Casteleyn
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package be.duncanc.discordmodbot.bot.commands

import be.duncanc.discordmodbot.bot.utils.JDALibHelper
import be.duncanc.discordmodbot.data.entities.UserGuildPoints
import be.duncanc.discordmodbot.data.repositories.UserGuildPointsRepository
import net.dv8tion.jda.core.MessageBuilder
import net.dv8tion.jda.core.Permission
import net.dv8tion.jda.core.entities.ChannelType
import net.dv8tion.jda.core.entities.Guild
import net.dv8tion.jda.core.entities.Message
import net.dv8tion.jda.core.entities.User
import net.dv8tion.jda.core.events.message.MessageReceivedEvent
import org.springframework.stereotype.Component
import java.util.concurrent.TimeUnit

@Component
class WarnPoints(
        val userGuildPointsRepository: UserGuildPointsRepository
) : CommandModule(
        arrayOf("WarnPoints"),
        null,
        null
) {
    override fun commandExec(event: MessageReceivedEvent, command: String, arguments: String?) {
        if (!event.isFromType(ChannelType.TEXT)) {
            throw IllegalArgumentException("This command can only be used in a guild/server channel.")
        }
        if (event.message.mentionedUsers.size == 1 && event.member.hasPermission(Permission.KICK_MEMBERS)) {
            val requestedUserPoints = userGuildPointsRepository.findById(UserGuildPoints.UserGuildPointsId(event.message.mentionedUsers[0].idLong, event.guild.idLong))
            if (requestedUserPoints.isPresent) {
                informUserOfPoints(event.author, requestedUserPoints.get(), event.guild)
                event.textChannel.sendMessage("The list of points the user collected has been send in a private message for privacy reason").queue(cleanUp())
            } else {
                event.textChannel.sendMessage("The user has not received any points.").queue(cleanUp())
            }
        } else {
            val userPoints = userGuildPointsRepository.findById(UserGuildPoints.UserGuildPointsId(event.author.idLong, event.guild.idLong))
            if (userPoints.isPresent) {
                informUserOfPoints(event.author, userPoints.get(), event.guild)
                event.textChannel.sendMessage("Your list of points has been send in a private message to you for privacy reasons, if you didn't receive any messages make sure you have enabled dm from server members on this server before executing this command.").queue(cleanUp())
            } else {
                event.textChannel.sendMessage("You haven't received any points. Good job!").queue(cleanUp())
            }
        }
    }

    private fun cleanUp(): (Message) -> Unit =
            { it.delete().queueAfter(1, TimeUnit.MINUTES) }

    private fun informUserOfPoints(user: User, userPoints: UserGuildPoints, guild: Guild) {
        user.openPrivateChannel().queue { privateChannel ->
            val message = MessageBuilder()
            message.append("Summary of points you received.")
            userPoints.points.forEach {
                message.append("\n\n").append(it.points!!).append(" point(s)")
                        .append(" on ").append(it.creationDate).append(" by ").append(JDALibHelper.getEffectiveNameAndUsername(guild.getMemberById(it.creatorId!!)))
            }
            message.buildAll(MessageBuilder.SplitPolicy.NEWLINE).forEach {
                privateChannel.sendMessage(it).queue()
            }
        }
    }
}