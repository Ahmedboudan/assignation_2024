package ma.nemo.assignment.repository;

import ma.nemo.assignment.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  @Query("SELECT p FROM Product p WHERE p.expirationDate >= CURRENT_DATE AND p.expirationDate <= :expiredDate")
  List<Product> findByExpirationDateGreaterThanEqualAndExpirationDateLessThanEqual(Date expiredDate);
  Product findByProductCode(String productCode);
}