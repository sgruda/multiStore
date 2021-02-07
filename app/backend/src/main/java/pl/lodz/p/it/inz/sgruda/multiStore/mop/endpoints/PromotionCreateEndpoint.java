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
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionCreateService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.enums.CategoryName;

import javax.validation.Valid;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/promotion")
public class PromotionCreateEndpoint {
    private PromotionCreateService promotionCreateService;

    @Autowired
    public PromotionCreateEndpoint(PromotionCreateService promotionCreateService) {
        this.promotionCreateService = promotionCreateService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> createPromotion(@Valid @RequestBody PromotionDTO promotionDTO, @CurrentUser UserPrincipal userPrincipal) {
        PromotionEntity promotionEntity = new PromotionEntity(
                promotionDTO.getName(),
                promotionDTO.getDiscount(),
                promotionDTO.isActive(),
                promotionDTO.getExpireDate(),
                userPrincipal.getEmail()
        );

        try {
            promotionCreateService.createPromotion(promotionEntity, CategoryName.valueOf(promotionDTO.getOnCategory()));
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "promotion.correctly.created"));
    }
}
