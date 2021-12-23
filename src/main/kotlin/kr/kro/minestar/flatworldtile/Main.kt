package kr.kro.minestar.flatworldtile

import org.bukkit.plugin.java.JavaPlugin

class Main : JavaPlugin() {
    companion object {
        lateinit var pl: Main
        const val prefix = "§f[§9PAPERING§f]"
    }

    override fun onEnable() {
        pl = this
        logger.info("$prefix §aEnable")
        getCommand("tile")?.setExecutor(CMD)
        saveResource("test.schem", false)
    }

    override fun onDisable() {
    }
}