package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.mop.ProductNotExistsException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.ProductGetService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.HashSimpleDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@Validated
@RequestMapping("/api/product")
public class ProductGetEndpoint {
    private ProductGetService productGetService;
    private HashSimpleDTO hashSimpleDTO;

    @Autowired
    public ProductGetEndpoint(ProductGetService productGetService, HashSimpleDTO hashSimpleDTO) {
        this.productGetService = productGetService;
        this.hashSimpleDTO = hashSimpleDTO;
    }

    @GetMapping
    public ResponseEntity<?> getProductByTitle(@Valid @NotNull(message = "validation.notnull")
                                               @Pattern(regexp = "[0-9a-zA-ZąćęłńóśźżĄĆĘŁŃÓŚŹŻ?!:., ]+", message = "validation.pattern")
                                               @Size(min = 1, max = 64, message = "validation.size")
                                               @RequestParam(value = "title") String title) {
        ProductEntity productEntity;
        try {
            productEntity = productGetService.getProductByTitle(title);
        } catch (ProductNotExistsException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        ProductMapper productMapper = new ProductMapper();
        ProductDTO productDTO = productMapper.toDTO(productEntity);
        hashSimpleDTO.hashDTO(productDTO);
        return ResponseEntity.ok(productDTO);
    }
}
