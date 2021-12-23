package kr.kro.minestar.flatworldtile.functions

import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import kr.kro.minestar.flatworldtile.Main.Companion.pl
import kr.kro.minestar.flatworldtile.Main.Companion.prefix
import kr.kro.minestar.utility.bool.BooleanScript
import kr.kro.minestar.utility.bool.addScript
import org.bukkit.World
import org.bukkit.entity.Player
import java.io.File
import java.io.FileInputStream

object Setting {
    fun setController(player: Player): BooleanScript {
        if (!player.isOp) return false.addScript("$prefix §cYou are not op.")
        if (Tile.controller != null) return false.addScript("$prefix §cController is not empty.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        Tile.controller = player
        return true.addScript("$prefix §aController registration success.")
    }

    fun setFile(player: Player, fileName: String): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        val file = File(pl.dataFolder, "$fileName.schem")
        if (!file.exists()) return false.addScript("$prefix §cFile is not exists.")
        val format = ClipboardFormats.findByFile(file) ?: return false.addScript("$prefix §cFile is invalid.")
        val clipboard = format.getReader(FileInputStream(file)).read()
        val vector = clipboard.dimensions
        Tile.offsetX = vector.x
        Tile.offsetZ = vector.z
        Tile.file = file
        return true.addScript("$prefix §aFile setup success.")
    }

    fun setY(player: Player, int: Int): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        if (int !in 0..255) return false.addScript("$prefix §eSetY §cmust be at least 0 and no more than 255.")
        Tile.setY = int
        return true.addScript("$prefix §aSetY setup success.")
    }

    fun setLimit(player: Player, int: Int): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        if (int <= 0) return false.addScript("$prefix §eLimit §cmust be greater than 0.")
        Tile.limit = int
        return true.addScript("$prefix §aLimit setup success.")
    }

    fun setCount(player: Player, int: Int): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (int <= 0) return false.addScript("$prefix §eCount §cmust be greater than 0.")
        Tile.count = int
        return true.addScript("$prefix §aCount setup success.")
    }

    fun setCycle(player: Player, int: Int): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        if (int <= 0) return false.addScript("$prefix §eCycle §cmust be greater than 0.")
        Tile.cycle = int.toLong()
        return true.addScript("$prefix §aCycle setup success.")
    }

    fun setWorldBorder(player: Player, boolean: Boolean): BooleanScript {
        if (Tile.controller != player) return false.addScript("$prefix §cYou are not controller.")
        if (Tile.task != null) return false.addScript("$prefix §cPapering is running.")
        Tile.setWorldBorder = boolean
        return true.addScript("$prefix §aFrom now on, SetWorldBorder is $boolean")
    }
}