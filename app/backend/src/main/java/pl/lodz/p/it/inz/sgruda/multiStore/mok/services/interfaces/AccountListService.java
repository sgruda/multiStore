package pl.lodz.p.it.inz.sgruda.multiStore.mok.services.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import pl.lodz.p.it.inz.sgruda.multiStore.entities.AccountEntity;

import java.util.List;

public interface AccountListService {
    Page<AccountEntity> getAccounts(Pageable pageable);
    Page<AccountEntity> getFilteredAccounts(String textToSearch, Pageable pageable, Boolean active);
    List<AccountEntity> getFilteredAccounts(String textToSearch, Sort sort, Boolean active);
}
