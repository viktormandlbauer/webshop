package at.fhtw.webshop.repository;

import at.fhtw.webshop.model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
    Optional<Voucher> findByCodeIgnoreCase(String code);
}
