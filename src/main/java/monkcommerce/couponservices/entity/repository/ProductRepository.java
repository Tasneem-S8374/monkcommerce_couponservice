package monkcommerce.couponservices.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import monkcommerce.couponservices.entity.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
}