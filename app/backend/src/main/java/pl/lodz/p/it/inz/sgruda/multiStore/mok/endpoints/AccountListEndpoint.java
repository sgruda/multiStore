package pl.lodz.p.it.inz.sgruda.multiStore.mok.endpoints;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mappers.mok.AccountMapper;
import pl.lodz.p.it.inz.sgruda.multiStore.dto.mok.AccountDTO;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;
import pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces.AccountListService;
import pl.lodz.p.it.inz.sgruda.multiStore.responses.ApiResponse;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.components.SignAccountDTOUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log
@RestController
@Transactional(
        propagation = Propagation.NEVER
)
@RequestMapping("/api/accounts")
public class AccountListEndpoint {
    private AccountListService accountListService;
    private SignAccountDTOUtil signAccountDTOUtil;

    @Autowired
    public AccountListEndpoint(AccountListService accountListService, SignAccountDTOUtil signAccountDTOUtil) {
        this.accountListService = accountListService;
        this.signAccountDTOUtil = signAccountDTOUtil;
    }

    @GetMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Map<String, Object>> getAccountsPage(
            @RequestParam(required = false) String textToSearch,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "lastName, desc") String[] sort) {

            List<Order> orders = new ArrayList<>();
        log.severe("Hello");
            if (sort[0].contains(",")) {
                // will sort more than 2 fields
                // sortOrder="field, direction"
                for (String sortOrder : sort) {
                    String[] _sort = sortOrder.split(",");
                    orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
                }
            } else {
                // sort=[field, direction]
                orders.add(new Order(getSortDirection(sort[1]), sort[0]));
            }

            Pageable pagingSort = PageRequest.of(page, size, Sort.by(orders));

            Page<AccountEntity> pageAccountEntities;
            if (textToSearch == null)
                pageAccountEntities = accountListService.getAccounts(pagingSort);
            else
                pageAccountEntities = accountListService.getAccountsByTextInNameOrEmail(textToSearch, pagingSort);

            List<AccountDTO> accountDTOS;
            AccountMapper accountMapper = new AccountMapper();
            accountDTOS = pageAccountEntities.getContent().stream()
                            .map(entity -> accountMapper.toDTO(entity))
                            .collect(Collectors.toList());
            accountDTOS.forEach(dto -> signAccountDTOUtil.signAccountDTO(dto));


            Map<String, Object> response = new HashMap<>();
            response.put("accounts", accountDTOS);
            response.put("currentPage", pageAccountEntities.getNumber());
            response.put("totalItems", pageAccountEntities.getTotalElements());
            response.put("totalPages", pageAccountEntities.getTotalPages());

//            return new ResponseEntity<>(response, HttpStatus.OK);
            return ResponseEntity.ok(response);
    }

    private Sort.Direction getSortDirection(String direction) {
        return direction.equals("asc") ? Sort.Direction.ASC :  Sort.Direction.DESC;
    }
}
