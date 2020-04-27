package net.waterraid.GenBucket;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.scheduler.BukkitRunnable;

public class BucketScheduling extends BukkitRunnable {
    public static int maxChanges = 20;
    Material _mat;
    int _x,_y,_z;
    Location _loc;
    int blockChanged = 0;
    public BucketScheduling(Material mat, Location loc, int x, int y, int z) {
        _mat = mat;
        _loc = loc;
        _x = x;
        _y = y;
        _z = z;
        _loc.getBlock().setType(mat);
    }
    @Override
    public void run() {
        _loc.add(_x,_y, _z);
        if (_loc.getBlock().getType().isOccluding()) {
            this.cancel();
            return;
        }
        if (_loc.getY() > 256 || _loc.getY() < 0) {
            this.cancel();
            return;
        }
        _loc.getBlock().setType(_mat);
        blockChanged++;
        if (blockChanged >= maxChanges) {
            this.cancel();
            return;
        }

    }

}
