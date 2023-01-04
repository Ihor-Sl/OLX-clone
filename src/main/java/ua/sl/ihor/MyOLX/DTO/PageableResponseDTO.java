package ua.sl.ihor.MyOLX.DTO;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PageableResponseDTO<T> {
    private int totalPages;
    private long totalElements;
    private List<T> content;

    public PageableResponseDTO(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.content = page.getContent();
    }
}
