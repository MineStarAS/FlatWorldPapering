package kr.kro.minestar.flatworldtile.functions

import com.sk89q.worldedit.WorldEdit
import com.sk89q.worldedit.bukkit.BukkitAdapter
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormats
import com.sk89q.worldedit.function.operation.Operations
import com.sk89q.worldedit.math.BlockVector3
import com.sk89q.worldedit.session.ClipboardHolder
import kr.kro.minestar.flatworldtile.Main.Companion.pl
import kr.kro.minestar.flatworldtile.Main.Companion.prefix
import kr.kro.minestar.utility.bool.BooleanScript
import kr.kro.minestar.utility.bool.addScript
import kr.kro.minestar.utility.string.toServer
import kr.kro.minestar.utility.time.ElapsedTime
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.entity.Player
import org.bukkit.scheduler.BukkitTask
import java.io.File
import java.io.FileInputStream
import kotlin.math.max

object Tile {
    var controller: Player? = null
    var file : File? = null
    var world: World = Bukkit.getWorlds().first()

    val list = mutableListOf<Pair<Int, Int>>()
    private var first = 0
    private var second = 0

    var offsetX = 1
    var offsetZ = 1
    var setY = world.spawnLocation.y.toInt()
    var limit = 5

    var count = 10
    var cycle = 20L

    var setWorldBorder = true

    private var max = 0
    fun percent() = list.size * 100 / max

    var lastCount = limit + 1
    var task: BukkitTask? = null

    fun run(player: Player): BooleanScript {
        if (controller != player) return false.addScript("$prefix §cYou are not a controller.")
        if (task != null) return false.addScript("$prefix §cPapering is currently in progress.")
        reset(player)
        val check = check()
        if (!check.boolean) return check
        if (setWorldBorder) {
            world.worldBorder.center = Location(world, 0.5, 0.0, 0.5)
            world.worldBorder.size = (limit * 2 + 1) * max(offsetX, offsetZ).toDouble()

        }
        val et1 = ElapsedTime()
        task = Bukkit.getScheduler().runTaskTimer(pl, Runnable {
            val et2 = ElapsedTime()
            for (i in 1..count) {
                if (list.size == (limit * 2 + 1) * (limit * 2 + 1)) {
                    "§aFinish!!".toServer()
                    task?.cancel()
                    task = null
                    "§etime elapsed : ${et1.toSecond()}".toServer()
                    break
                }
                val pair = getEmptyPosition()
                val loc = Location(world, pair.first * offsetX.toDouble(), setY.toDouble(), pair.second * offsetZ.toDouble())
                paste(loc)
                list.add(pair)
            }
            if (task != null) "§e[${percent()}%] §fDelay : ${et2.toSecond()}".toServer()
        }, 0, cycle)
        return true.addScript("$prefix Total Count : $max")
    }

    fun stop(player: Player): BooleanScript {
        if (controller != player) return false.addScript("$prefix §cYou are not a controller.")
        if (task == null) return false.addScript("$prefix §cIt is not running.")
        task!!.cancel()
        task = null
        return true.addScript("$prefix §cStopped.")
    }

    fun reset(player: Player) {
        list.clear()
        world = player.world
        first = 0
        second = 0
        task = null
        lastCount = limit + 1
        max = (limit * 2 + 1) * (limit * 2 + 1)
    }

    fun check(): BooleanScript {
        if (file == null) return false.addScript("$prefix §cFile is null.")
        if (offsetX <= 0) return false.addScript("$prefix §eOffsetX §cmust be greater than 0.")
        if (offsetZ <= 0) return false.addScript("$prefix §eOffsetZ §cmust be greater than 0.")
        if (setY !in 0..255) return false.addScript("$prefix §eSetY §cmust be at least 0 and no more than 255.")
        if (limit <= 0) return false.addScript("$prefix §eLimit §cmust be greater than 0.")
        if (count <= 0) return false.addScript("$prefix §eCount §cmust be greater than 0.")
        if (cycle <= 0) return false.addScript("$prefix §eCycle §cmust be greater than 0.")
        return true.addScript("")
    }

    fun paste(loc: Location) {
        val format = ClipboardFormats.findByFile(file)
        val clipboard = format!!.getReader(FileInputStream(file)).read()
        val world = BukkitAdapter.adapt(loc.world)
        clipboard.dimensions
        val editSession = WorldEdit.getInstance().editSessionFactory.getEditSession(world, -1)
        val operation = ClipboardHolder(clipboard)
            .createPaste(editSession)
            .to(BlockVector3.at(loc.blockX, loc.blockY, loc.blockZ))
            .ignoreAirBlocks(false)
            .build()
        Operations.complete(operation)
        editSession.flushSession()
    }

    fun getEmptyPosition(): Pair<Int, Int> {
        if (second > limit) {
            first = second - lastCount
            second = limit
            if (lastCount > 0) --lastCount
        }
        if (first > limit) {
            first = second - lastCount
            second = limit
            if (lastCount > 0) --lastCount
        }
        if (!list.contains(Pair(first, second))) return Pair(first, second)
        if (!list.contains(Pair(-first, -second))) return Pair(-first, -second)
        if (!list.contains(Pair(-first, second))) return Pair(-first, second)
        if (!list.contains(Pair(first, -second))) return Pair(first, -second)
        ++first
        --second
        if (second < 0) {
            first = 0
            second = lastSecondOver()
        }
        return getEmptyPosition()
    }

    fun lastSecondOver(): Int {
        val l = getSecondList()
        if (l.isEmpty()) return 0
        return l.sorted().last() + 1
    }

    fun getSecondList(): List<Int> {
        val keys = mutableListOf<Int>()
        for (pair in list) if (!keys.contains(pair.second)) keys.add(pair.second)
        return keys
    }
}