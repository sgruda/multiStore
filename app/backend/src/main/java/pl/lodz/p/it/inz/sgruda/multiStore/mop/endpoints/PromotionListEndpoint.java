package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.PromotionMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionListService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;

import java.util.List;
import java.util.stream.Collectors;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@Validated
@RequestMapping("/api/promotions")
public class PromotionListEndpoint {
    private PromotionListService promotionListService;
    private SignSimpleDTO signSimpleDTO;

    @Autowired
    public PromotionListEndpoint(PromotionListService promotionListService, SignSimpleDTO signSimpleDTO) {
        this.promotionListService = promotionListService;
        this.signSimpleDTO = signSimpleDTO;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_EMPLOYEE')")
    public ResponseEntity<List<PromotionDTO>> getAllPromotions() {
        List<PromotionEntity> entities = promotionListService.getAllPromotions();
        PromotionMapper promotionMapper = new PromotionMapper();
        List<PromotionDTO> dtos = entities.stream()
                .map(entity -> {
                    PromotionDTO dto = promotionMapper.toDTO(entity);
                    signSimpleDTO.signDTO(dto);
                    return dto;
                })
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
