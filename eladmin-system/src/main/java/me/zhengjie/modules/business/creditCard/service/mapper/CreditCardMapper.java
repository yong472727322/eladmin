package me.zhengjie.modules.business.creditCard.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.business.creditCard.domain.CreditCard;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author leo
* @date 2019-12-25
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CreditCardMapper extends BaseMapper<CreditCardDTO, CreditCard> {

}