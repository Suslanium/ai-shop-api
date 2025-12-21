package testtask.shift.shopapi.service;

import org.springframework.stereotype.Service;
import testtask.shift.shopapi.model.analytics.InventoryCountsResponse;
import testtask.shift.shopapi.repository.HardDriveRepository;
import testtask.shift.shopapi.repository.LaptopRepository;
import testtask.shift.shopapi.repository.MonitorRepository;
import testtask.shift.shopapi.repository.PersonalComputerRepository;

@Service
public class AnalyticsService {
    private final HardDriveRepository hardDriveRepository;
    private final LaptopRepository laptopRepository;
    private final MonitorRepository monitorRepository;
    private final PersonalComputerRepository personalComputerRepository;

    public AnalyticsService(HardDriveRepository hardDriveRepository,
                            LaptopRepository laptopRepository,
                            MonitorRepository monitorRepository,
                            PersonalComputerRepository personalComputerRepository) {
        this.hardDriveRepository = hardDriveRepository;
        this.laptopRepository = laptopRepository;
        this.monitorRepository = monitorRepository;
        this.personalComputerRepository = personalComputerRepository;
    }

    public InventoryCountsResponse getInventoryCounts() {
        long hardDrives = hardDriveRepository.count();
        long laptops = laptopRepository.count();
        long monitors = monitorRepository.count();
        long personalComputers = personalComputerRepository.count();
        long total = hardDrives + laptops + monitors + personalComputers;

        return new InventoryCountsResponse(total, hardDrives, laptops, monitors, personalComputers);
    }
}
