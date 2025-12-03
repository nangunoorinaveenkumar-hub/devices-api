package com.naveen.devices.repository;

import com.naveen.devices.domain.Device;
import com.naveen.devices.domain.DeviceState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    List<Device> findByBrand(String brand);
    List<Device> findByState(DeviceState state);
}
