package pl.lodz.p.it.inz.sgruda.multiStore.moz.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.moz.BasketMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.moz.BasketDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.moz.BasketEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.AppBaseException;
import pl.lodz.p.it.inz.sgruda.multiStore.moz.services.interfaces.BasketDetailsService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.security.CurrentUser;
import pl.lodz.p.it.inz.sgruda.multiStore.security.UserPrincipal;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.moz.SignMozDTOUtil;


@Log
@Validated
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/basket")
public class BasketDetailsEndpoint {
    private BasketDetailsService basketDetailsService;
    private SignMozDTOUtil signMozDTOUtil;

    @Autowired
    public BasketDetailsEndpoint(BasketDetailsService basketDetailsService, SignMozDTOUtil signMozDTOUtil) {
        this.basketDetailsService = basketDetailsService;
        this.signMozDTOUtil = signMozDTOUtil;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_CLIENT')")
    public ResponseEntity<?> getOwnBasket(@CurrentUser UserPrincipal currentUser) {
        BasketEntity basketEntity;
        try {
            basketEntity = basketDetailsService.getBasketEntityByOwnerEmail(currentUser.getEmail());
        } catch (AppBaseException e) {
            log.severe("Error: " + e);
            return new ResponseEntity(new ApiResponse(false, e.getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        BasketMapper basketMapper = new BasketMapper();
        BasketDTO basketDTO = basketMapper.toDTO(basketEntity);
        signMozDTOUtil.signBasketDTO(basketDTO);
        return ResponseEntity.ok(basketDTO);
    }
}
