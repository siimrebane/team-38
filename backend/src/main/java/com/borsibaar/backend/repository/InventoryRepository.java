package com.borsibaar.backend.repository;

import com.borsibaar.backend.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByOrganizationIdAndProductId(Long organizationId, Long productId);

    List<Inventory> findByOrganizationId(Long organizationId);

    boolean existsByProductId(Long productId);
}
