package me.zhengjie.modules.business.order.service.mapper;

import me.zhengjie.base.BaseMapper;
import me.zhengjie.modules.business.order.domain.LeoOrder;
import me.zhengjie.modules.business.order.service.dto.LeoOrderDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

/**
* @author leo
* @date 2019-12-19
*/
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface LeoOrderMapper extends BaseMapper<LeoOrderDTO, LeoOrder> {

}