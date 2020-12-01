package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.ProductMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductListService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@Validated
@RequestMapping("/api/products")
public class ProductListEndpoint {
    private ProductListService productListService;
    private SignSimpleDTO signSimpleDTO;

    @Autowired
    public ProductListEndpoint(ProductListService productListService, SignSimpleDTO signSimpleDTO) {
        this.productListService = productListService;
        this.signSimpleDTO = signSimpleDTO;
    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getProductsPage(
            @Valid @Pattern(regexp = "[0-9a-zA-Z!@#$%^&*()]+", message = "validation.pattern") @RequestParam(required = false) String textToSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(required = false) Boolean active,
            @Pattern(regexp = "(book|movie)", message = "validation.pattern") @RequestParam(required = false) String type) {

        Pageable paging = PageRequest.of(page, size);

        Page<ProductEntity> pageProductEntities;
        pageProductEntities = productListService.getFilteredProducts(textToSearch, paging, active, type != null ? ProductType.valueOf(type) : null);

        ProductMapper productMapper = new ProductMapper();
        List<ProductDTO> productDTOS;
        productDTOS = pageProductEntities.getContent().stream()
                .map(entity -> productMapper.toDTO(entity))
                .collect(Collectors.toList());
        productDTOS.forEach(dto -> signSimpleDTO.signDTO(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("products", productDTOS);
        response.put("currentPage", pageProductEntities.getNumber());
        response.put("totalItems", pageProductEntities.getTotalElements());
        response.put("totalPages", pageProductEntities.getTotalPages());

        return ResponseEntity.ok(response);
    }

}
