package kr.kro.minestar.flatworldtile

import kr.kro.minestar.flatworldtile.Main.Companion.prefix
import kr.kro.minestar.flatworldtile.functions.Setting
import kr.kro.minestar.flatworldtile.functions.Tile
import kr.kro.minestar.utility.string.toPlayer
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player


object CMD : CommandExecutor, TabCompleter {
    private val args0 = listOf("run", "stop", "set")
    private val set = listOf("controller", "file", "y", "limit", "count", "cycle", "worldborder")
    override fun onCommand(player: CommandSender, cmd: Command, label: String, args: Array<out String>): Boolean {
        if (player !is Player) return false
        if (!player.isOp) return false
        if (args.isEmpty()) "tile".toPlayer(player).also { return false }
        when (args.first()) {
            args0[0] -> Tile.run(player).script.toPlayer(player)
            args0[1] -> Tile.stop(player).script.toPlayer(player)
            args0[2] -> {
                if (args.size == 1) {
                    "$prefix §e:: Setting ::".toPlayer(player)
                    " ".toPlayer(player)
                    "§9Controller §f: ${Tile.controller?.name}".toPlayer(player)
                    "§9File §f: ${Tile.file?.name}".toPlayer(player)
                    "§9Y §f: ${Tile.setY}".toPlayer(player)
                    "§9Y §f: ${Tile.limit}".toPlayer(player)
                    "§9Count §f: ${Tile.count}".toPlayer(player)
                    "§9Cycle §f: ${Tile.cycle}".toPlayer(player)
                    "§9WorldBorder §f: ${Tile.setWorldBorder}".toPlayer(player)
                    return false
                }
                when (args[1]) {
                    set[0] -> {
                        if (args.size != 2) "$prefix §c/tile set ${args[1]}".toPlayer(player).also { return false }
                        Setting.setController(player).script.toPlayer(player)
                    }
                    set[1] -> {
                        if (args.size != 3) "$prefix §c/tile set ${args[1]} <FileName>".toPlayer(player).also { return false }
                        Setting.setFile(player, args.last()).script.toPlayer(player)
                    }
                    set[2] -> {
                        if (args.size != 3) "$prefix §c/tile set ${args[1]} <Number>".toPlayer(player).also { return false }
                        val int = args.last().toIntOrNull()
                        int ?: "$prefix §e${args.last()} §cis not number.".toPlayer(player).also { return false }
                        Setting.setY(player, int!!).script.toPlayer(player)
                    }
                    set[3] -> {
                        if (args.size != 3) "$prefix §c/tile set ${args[1]} <Number>".toPlayer(player).also { return false }
                        val int = args.last().toIntOrNull()
                        int ?: "$prefix §e${args.last()} §cis not number.".toPlayer(player).also { return false }
                        Setting.setLimit(player, int!!).script.toPlayer(player)
                    }
                    set[4] -> {
                        if (args.size != 3) "$prefix §c/tile set ${args[1]} <Number>".toPlayer(player).also { return false }
                        val int = args.last().toIntOrNull()
                        int ?: "$prefix §e${args.last()} §cis not number.".toPlayer(player).also { return false }
                        Setting.setCount(player, int!!).script.toPlayer(player)
                    }
                    set[5] -> {
                        if (args.size != 3) "$prefix §c/tile set ${args[1]} <Number>".toPlayer(player).also { return false }
                        val int = args.last().toIntOrNull()
                        int ?: "$prefix §e${args.last()} §cis not number.".toPlayer(player).also { return false }
                        Setting.setCycle(player, int!!).script.toPlayer(player)
                    }
                    set[6] -> {
                        if (args.size > 2) "$prefix §c/tile set ${args[1]}".toPlayer(player).also { return false }
                        Setting.setWorldBorder(player, !Tile.setWorldBorder).script.toPlayer(player)
                    }
                }
            }
        }
        return false
    }

    override fun onTabComplete(p: CommandSender, cmd: Command, alias: String, args: Array<out String>): MutableList<String> {
        val list = mutableListOf<String>()
        if (args.size == 1) for (s in args0) if (s.contains(args.last())) list.add(s)
        when (args.first()) {
            args0[0] -> {}
            args0[1] -> {}
            args0[2] -> if (args.size == 2) for (s in set) if (s.contains(args.last())) list.add(s)
        }
        return list
    }

}