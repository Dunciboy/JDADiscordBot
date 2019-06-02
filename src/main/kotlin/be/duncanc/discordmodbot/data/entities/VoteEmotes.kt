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

package be.duncanc.discordmodbot.data.entities

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "voting_emotes")
data class VoteEmotes(
    @Id
    val guildId: Long? = null,
    @Column(nullable = false)
    val voteYesEmote: Long? = null,
    @Column(nullable = false)
    val voteNoEmote: Long? = null
) {


    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as VoteEmotes

        if (guildId != other.guildId) return false

        return true
    }

    override fun hashCode(): Int {
        return guildId.hashCode()
    }

    override fun toString(): String {
        return "VoteEmotes(guildId=$guildId, voteYesEmote=$voteYesEmote, voteNoEmote=$voteNoEmote)"
    }
}