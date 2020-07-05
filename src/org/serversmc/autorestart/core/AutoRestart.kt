package org.serversmc.autorestart.core

import org.bukkit.*
import org.bukkit.configuration.file.*
import org.bukkit.plugin.java.*
import org.serversmc.autorestart.cmds.*
import org.serversmc.autorestart.cmds.autore.*
import org.serversmc.autorestart.events.*
import org.serversmc.autorestart.support.*
import org.serversmc.autorestart.threads.*
import org.serversmc.autorestart.utils.*
import org.serversmc.autorestart.utils.Console
import java.io.*
import java.net.*

lateinit var PLUGIN: Main

class Main : JavaPlugin() {
	
	override fun onEnable() {
		// Initialize libraries
		Metrics(this, 2345)
		PLUGIN = this
		// Check if PlaceholderAPI is installed
		if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) PAPI.register()
		// Try to enable plugin
		try {
			// Setup plugin folder is does not exist
			dataFolder.mkdirs()
			// Configuration Files
			Config.init()
			// Event Register
			Bukkit.getPluginManager().apply {
				registerEvents(EventPlayerJoin, PLUGIN)
			}
			// Command Setup
			registerCommands()
			// Check for updates
			UpdateChecker.checkUpdate()
			// Display update message after server Done
			Bukkit.getScheduler().scheduleSyncDelayedTask(this) {
				when {
					UpdateChecker.hasUpdate() == null -> Console.warn("No Internet to check for updates")
					UpdateChecker.hasUpdate()!! -> Console.warn("There is a new version of AutoRestart! Go get it now! Latest version: v${UpdateChecker.getLatestVersion()}")
					else -> Console.info("Up to date!")
				}
			}
			// Timer Thread
			MainThread.run()
			// Done
			Console.info("Loaded")
		} catch (e: Exception) {
			Console.catchError(e, "UNFILTERED ERROR")
		}
	}
	
	override fun onDisable() {
		Console.info("Done")
	}
	
	private fun registerCommands() {
		// Main Command
		CAutoRestart.register()
		// Autore sub commands
		CHelp.register()
		CIn.register()
		CNow.register()
		CPause.register()
		CReload.register()
		CResume.register()
		CTime.register()
	}
	
}

object UpdateChecker {
	
	private var LATEST_BUILD: Int? = null
	private var LATEST_VERSION: String? = null
	private var UPDATE_FOUND: Boolean? = null
	
	private val url = URL("https://gitlab.com/dennislysenko/autorestart-v4/-/raw/master/res/plugin.yml")
	private val pluginYml = InputStreamReader(PLUGIN.getResource("plugin.yml") as InputStream)
	private val yaml = YamlConfiguration.loadConfiguration(pluginYml)
	
	fun checkUpdate() {
		try {
			// Fetch latest version string
			YamlConfiguration().apply {
				load(InputStreamReader(url.openStream()))
				LATEST_BUILD = getInt("build")
				LATEST_VERSION = getString("version")?.trim() ?: "null"
			}
			// Check if latest build was fetched
			if (LATEST_BUILD == null) return
			// Check if version is up to date
			val currentBuild = yaml.getInt("build", -1)
			if (currentBuild == -1) return
			UPDATE_FOUND = LATEST_BUILD!! > currentBuild
		} catch (e: Exception) {}
	}
	
	fun hasUpdate() = UPDATE_FOUND
	
	fun getLatestVersion() = LATEST_VERSION ?: "N/A"
	
}