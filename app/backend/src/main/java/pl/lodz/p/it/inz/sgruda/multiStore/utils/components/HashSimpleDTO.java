package pl.lodz.p.it.inz.sgruda.multiStore.utils.components;

import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.lodz.p.it.inz.sgruda.multiStore.utils.interfaces.HashVerifiability;


@Log
@Component
public class HashSimpleDTO {
    private HashDTOUtil hashDTOUtil;

    @Autowired
    public HashSimpleDTO(HashDTOUtil hashDTOUtil) {
        this.hashDTOUtil = hashDTOUtil;
    }

    public void hashDTO(HashVerifiability dto) {
        if (dto != null) {
            hashDTOUtil.hashDTO(dto);
        }
    }
}