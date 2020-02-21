package org.serversmc.autorestart.cmds

import org.bukkit.command.*
import org.bukkit.entity.*
import org.serversmc.autorestart.cmds.autore.*
import org.serversmc.autorestart.core.Main.Companion.AutoRestart
import org.serversmc.autorestart.interfaces.*
import org.serversmc.utils.ChatColor.GRAY
import org.serversmc.utils.ChatColor.RED
import org.serversmc.utils.Console.catchError

object CAutoRestart : ICommand {
	
	val subCommands = ArrayList<ICommand>().apply {
		add(CHelp)
		add(CIn)
		add(CNow)
		add(CPause)
		add(CReload)
		add(CResume)
		add(CTime)
	}
	
	override fun execute(sender: CommandSender, args: MutableList<out String>) {
		// Global command header
		sender.sendMessage("${RED}AutoRestart ${GRAY}- v${AutoRestart.description.version}")
		// Check if argument length requirement meet
		if (args.isNotEmpty()) {
			// Sub command iteration
			subCommands.forEach {
				// Check if label matches sub command label
				if (it.getLabel().equals(args[0], true)) {
					// Check if sender has permission
					if (!sender.hasPermission(it.getPermission())) {
						// Not enough permissions error
						sender.sendMessage("${RED}You do not have permission to use this command!")
						return@forEach
					}
					try {
						it.execute(sender, args.apply { removeAt(0) })
					} catch (e: Exception) {
						catchError(e, "CAutoRestart.onCommand():${it.getLabel()}")
					}
					return@execute
				}
			}
		}
		else sender.sendMessage("${RED}Not enough arguments. Try: /autore help")
	}
	
	override fun tabComplete(player: Player, args: MutableList<out String>): MutableList<String>? {
		subCommands.forEach {
			if (!player.hasPermission(getPermission())) return@forEach
			if (it.getLabel().equals(args[0], true) and (args.size > 1)) return it.tabComplete(player, args.apply { removeAt(0) })
		}
		return ArrayList<String>().apply {
			subCommands.forEach {
				if (!player.hasPermission(getPermission())) return@forEach
				if (it.getLabel().toLowerCase().startsWith(args[0].toLowerCase())) {
					add(args.last() + it.getLabel().toLowerCase().substring(args.last().length))
				}
			}
		}
	}
	
	override fun getLabel(): String = "AUTORE"
	override fun getPermission(): String = "autorestart"
	override fun getUsage(): String = "/autore <sub_command>"
	override fun getDescription(): String = "Main AutoRestart command"
	
}