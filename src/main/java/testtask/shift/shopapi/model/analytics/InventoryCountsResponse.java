package testtask.shift.shopapi.model.analytics;

public class InventoryCountsResponse {
    private final long total;
    private final long hardDrives;
    private final long laptops;
    private final long monitors;
    private final long personalComputers;

    public InventoryCountsResponse(long total,
                                   long hardDrives,
                                   long laptops,
                                   long monitors,
                                   long personalComputers) {
        this.total = total;
        this.hardDrives = hardDrives;
        this.laptops = laptops;
        this.monitors = monitors;
        this.personalComputers = personalComputers;
    }

    public long getTotal() {
        return total;
    }

    public long getHardDrives() {
        return hardDrives;
    }

    public long getLaptops() {
        return laptops;
    }

    public long getMonitors() {
        return monitors;
    }

    public long getPersonalComputers() {
        return personalComputers;
    }
}
