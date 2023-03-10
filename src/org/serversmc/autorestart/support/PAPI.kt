package org.serversmc.autorestart.support

import me.clip.placeholderapi.expansion.*
import org.bukkit.entity.*
import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.objects.*

object PAPI : PlaceholderExpansion() {
	
	override fun getVersion(): String = PLUGIN.description.version
	override fun getAuthor(): String = PLUGIN.description.authors.joinToString(", ")
	override fun getIdentifier(): String = "autorestart"
	override fun persist(): Boolean = true
	override fun canRegister(): Boolean = true
	
	override fun onPlaceholderRequest(p: Player?, params: String?): String? {
		if (params == null) return null
		return when (params.toLowerCase()) {
			"time_formatted1" -> "${HMS.H}h ${HMS.M}m ${HMS.S}s"
			"time_formatted2" -> "${HMS.H} hours ${HMS.M} minutes ${HMS.S} seconds"
			"time_formatted3" -> "${HMS.H}:${HMS.M}"
			"time_formatted4" -> "${HMS.H}:${HMS.M}:${HMS.S}"
			"time_hour" -> "${HMS.H}"
			"time_minute" -> "${HMS.M}"
			"time_seconds" -> "${HMS.S}"
			"time_raw_hour" -> "${HMS.rawH}"
			"time_raw_minute" -> "${HMS.rawM}"
			"time_raw_seconds" -> "${HMS.rawS}"
			else -> null
		}
	}
	
}
