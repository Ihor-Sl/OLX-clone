package ua.sl.ihor.MyOLX.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ua.sl.ihor.MyOLX.domain.ProductPhoto;

@Repository
public interface ProductPhotoRepository extends JpaRepository<ProductPhoto, Long> {
}
