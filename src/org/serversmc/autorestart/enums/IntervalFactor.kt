package org.serversmc.autorestart.enums

import org.serversmc.autorestart.core.*
import org.serversmc.autorestart.utils.*
import org.serversmc.utils.*

enum class IntervalFactor {
	HOURS {
		
		override fun calculate() {
			TimerThread.TIME = (Config.Main_Modes_Interval_Value * 3600.0).toInt()
		}
		
	},
	DAYS {
		
		override fun calculate() {
			TimerThread.TIME = (Config.Main_Modes_Interval_Value * 86400.0).toInt()
		}
		
	},
	NONE {
		
		override fun calculate() {
			Console.err("Interval factor '${Config.Main_Modes_Interval_Factor_Raw}' in 'Main.yml:main.interval.factor' was not found! Switching to 'hours' factor!")
			HOURS.calculate()
		}
		
	};
	
	abstract fun calculate()
	
	companion object {
		fun parse(name: String): IntervalFactor {
			return try {
				valueOf(name.toUpperCase())
			} catch (e: IllegalArgumentException) {
				NONE
			}
		}
	}
	
}