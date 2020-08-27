package me.zhengjie.modules.business.order.service.dto;

import lombok.Data;
import java.io.Serializable;


/**
* @author leo
* @date 2019-12-19
*/
@Data
public class LeoOrderDTO implements Serializable {

    // ID
    private Long id;

    // 订单名称
    private String name;
}