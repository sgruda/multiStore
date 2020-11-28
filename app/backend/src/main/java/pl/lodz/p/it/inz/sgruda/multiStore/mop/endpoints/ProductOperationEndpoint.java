package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
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
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.ProductMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.ProductDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.ProductEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductEditService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;


import javax.validation.Valid;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/product")
public class ProductOperationEndpoint {
    private ProductEditService productEditService;
    private CheckerSimpleDTO checkerSimpleDTO;

    @Autowired
    public ProductOperationEndpoint(ProductEditService productEditService, CheckerSimpleDTO checkerSimpleDTO) {
        this.productEditService = productEditService;
        this.checkerSimpleDTO = checkerSimpleDTO;
    }

    @PostMapping("/edit")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> editProduct(@Valid @RequestBody ProductDTO productDTO) {
        ProductEntity productEntity;
        try {
            checkerSimpleDTO.checkSignature(productDTO);
            productEntity = productEditService.getProductByTitle(productDTO.getTitle());
            checkerSimpleDTO.checkVersion(productEntity, productDTO);
            ProductMapper productMapper = new ProductMapper();
            productMapper.updateEntity(productEntity, productDTO);
            productEditService.editProduct(productEntity);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "product.correctly.edited"));
    }
}
