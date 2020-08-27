package me.zhengjie.modules.business.creditCard.service.dto;

import lombok.Data;
import me.zhengjie.annotation.Query;

/**
* @author leo
* @date 2019-12-25
*/
@Data
public class CreditCardQueryCriteria{

    // Label
    @Query
    private String cardLabel;

    @Query(type = Query.Type.GREATER_THAN,propName = "createdDate")
    private String startTime;

    @Query(type = Query.Type.LESS_THAN,propName = "createdDate")
    private String endTime;

}