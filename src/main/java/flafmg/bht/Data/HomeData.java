package flafmg.bht.Data;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.Vector;

public class HomeData {
    private String homeName;
    private String worldName;
    private double x;
    private double y;
    private double z;
    private float yaw;
    private float pitch;
    private boolean status;

    public HomeData(String homeName, Location location, boolean status) {
        this.homeName = homeName;
        this.worldName = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
        this.status = status;
    }

    public String getHomeName() {
        return homeName;
    }

    public String getWorldName() {
        return worldName;
    }

    public boolean isPublic() {
        return status;
    }

    public void changeStatus(boolean status) {
        this.status = status;
    }

    public Location getLocation() {
        World world = Bukkit.getWorld(worldName);
        if (world != null) {
            return new Location(world, x, y, z, yaw, pitch);
        } else {
            return null;
        }
    }

}
