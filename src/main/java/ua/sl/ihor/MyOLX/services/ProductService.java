package ua.sl.ihor.MyOLX.services;

import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ua.sl.ihor.MyOLX.DTO.PageableResponseDTO;
import ua.sl.ihor.MyOLX.DTO.ProductFullInfoDTO;
import ua.sl.ihor.MyOLX.DTO.ProductPreviewDTO;
import ua.sl.ihor.MyOLX.domain.Product;

import java.io.IOException;
import java.util.List;

public interface ProductService {
    PageableResponseDTO<ProductPreviewDTO> page(Pageable pageable);

    List<ProductPreviewDTO> all();

    Product findById(long id);

    ProductFullInfoDTO findProductFullInfoById(long id);

    ProductFullInfoDTO add(Product newProduct, MultipartFile previewPhoto, List<MultipartFile> photosList) throws IOException;

    ProductFullInfoDTO update(long productId, Product updatedProduct, long userId);

    void delete(long id, long userId);
}
