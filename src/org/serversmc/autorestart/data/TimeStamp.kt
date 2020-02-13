package org.serversmc.autorestart.data

import org.serversmc.autorestart.utils.Console.err
import java.lang.Integer.*

data class TimeStamp(val h: Int, val s: Int)

object TimeStampManager {
	
	fun parseStringList(list: MutableList<String>): MutableList<TimeStamp> {
		return ArrayList<TimeStamp>().apply {
			list.forEach {
				try {
					val h = parseInt(it.split(":")[0])
					val m = parseInt(it.split(":")[1])
					add(TimeStamp(h, m))
				} catch (e: Exception) {
					err("Could not read \"$it\" please check Main.yml:main.modes.interval")
				}
			}
		}
	}
	
}