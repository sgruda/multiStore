package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.PromotionMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionActivityService;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionDeleteService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.CheckerSimpleDTO;

import javax.validation.Valid;

@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/promotion")
public class PromotionOperationEndpoint {
    private PromotionActivityService promotionActivityService;
    private PromotionDeleteService promotionDeleteService;
    private CheckerSimpleDTO checkerSimpleDTO;

    @Autowired
    public PromotionOperationEndpoint(PromotionActivityService promotionActivityService, PromotionDeleteService promotionDeleteService, CheckerSimpleDTO checkerSimpleDTO) {
        this.promotionActivityService = promotionActivityService;
        this.promotionDeleteService = promotionDeleteService;
        this.checkerSimpleDTO = checkerSimpleDTO;
    }

    @PutMapping("/block")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> blockPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionEntity promotionEntity;
        try {
            checkerSimpleDTO.checkHash(promotionDTO);
            promotionEntity = promotionActivityService.getPromotionByName(promotionDTO.getName());
            PromotionMapper promotionMapper = new PromotionMapper();
            PromotionEntity entityCopy = promotionMapper.createCopyOf(promotionEntity, promotionDTO);
            promotionActivityService.blockPromotion(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "promotion.correctly.blocked"));
    }

    @PutMapping("/unblock")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> unblockPromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionEntity promotionEntity;
        try {
            checkerSimpleDTO.checkHash(promotionDTO);
            promotionEntity = promotionActivityService.getPromotionByName(promotionDTO.getName());
            PromotionMapper promotionMapper = new PromotionMapper();
            PromotionEntity entityCopy = promotionMapper.createCopyOf(promotionEntity, promotionDTO);
            promotionActivityService.unblockPromotion(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "promotion.correctly.unblocked"));
    }

    @PostMapping("/delete")
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<?> deletePromotion(@Valid @RequestBody PromotionDTO promotionDTO) {
        PromotionEntity promotionEntity;
        try {
            checkerSimpleDTO.checkHash(promotionDTO);
            promotionEntity = promotionDeleteService.getPromotionByName(promotionDTO.getName());
            PromotionMapper promotionMapper = new PromotionMapper();
            PromotionEntity entityCopy = promotionMapper.createCopyOf(promotionEntity, promotionDTO);
            promotionDeleteService.deletePromotion(entityCopy);
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return ResponseEntity.ok(new ApiResponse(true, "promotion.correctly.deleted"));
    }
}
