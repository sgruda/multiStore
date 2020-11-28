package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductCreateService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.ProductType;


import javax.mail.MessagingException;
import javax.validation.Valid;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/product")
public class ProductDetailsEndoint {
    private ProductCreateService productCreateService;

    public ProductDetailsEndoint(ProductCreateService productCreateService) {
        this.productCreateService = productCreateService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductEntity productEntity = new ProductEntity(
                productDTO.getTitle(),
                productDTO.getDescription(),
                productDTO.getInStore(),
                productDTO.getPrice(),
                ProductType.valueOf(productDTO.getType())
        );
        try {
            productCreateService.createProduct(productEntity, CategoryName.valueOf(productDTO.getCategory()));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }


        return ResponseEntity.ok(new ApiResponse(true, "product.correctly.created"));
    }
}
