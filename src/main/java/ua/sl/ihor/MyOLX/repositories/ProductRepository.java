package ua.sl.ihor.MyOLX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sl.ihor.MyOLX.domain.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    boolean existsById(long id);
}
