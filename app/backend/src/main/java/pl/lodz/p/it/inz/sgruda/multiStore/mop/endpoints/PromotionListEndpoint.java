package pl.lodz.p.it.inz.sgruda.multiStore.mop.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mop.PromotionMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mop.PromotionDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mop.PromotionEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mop.services.interfaces.PromotionListService;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignSimpleDTO;

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
    public ResponseEntity<Map<String, Object>> getPromotionsPage(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable paging = PageRequest.of(page, size);
        Page<PromotionEntity> pagePromotionEntitites = promotionListService.getAllPromotions(paging);
        PromotionMapper promotionMapper = new PromotionMapper();
        List<PromotionDTO> dtos = pagePromotionEntitites.stream()
                .map(entity -> promotionMapper.toDTO(entity))
                .collect(Collectors.toList());
        dtos.forEach(dto -> signSimpleDTO.signDTO(dto));

        Map<String, Object> response = new HashMap<>();
        response.put("promotions", dtos);
        response.put("currentPage", pagePromotionEntitites.getNumber());
        response.put("totalItems", pagePromotionEntitites.getTotalElements());
        response.put("totalPages", pagePromotionEntitites.getTotalPages());

        return ResponseEntity.ok(response);
    }
}
