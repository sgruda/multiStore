package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.OptimisticLockAppException;
import pl.lodz.p.it.inz.sgruda.multiStore.exceptions.dto.DTOHashException;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.VersionGetter;

@Component
public class CheckerSimpleDTO {
    private HashDTOUtil hashDTOUtil;

    @Autowired
    public CheckerSimpleDTO(HashDTOUtil hashDTOUtil) {
        this.hashDTOUtil = hashDTOUtil;
    }

    public void checkHash(HashVerifiability dto) throws DTOHashException {
        if(dto != null) {
            this.checkHashSingleDTO(dto);
        }
    }
    protected void checkHashSingleDTO(HashVerifiability dto) throws DTOHashException {
        if(!hashDTOUtil.checkHashDTO(dto)) {
            throw new DTOHashException();
        }
    }
}
