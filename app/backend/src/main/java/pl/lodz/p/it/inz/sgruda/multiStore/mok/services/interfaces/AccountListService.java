package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.mok.AccountEntity;

public interface AccountListService {
    Page<AccountEntity> getFilteredAccounts(String textToSearch, Pageable pageable, Boolean active);
}
