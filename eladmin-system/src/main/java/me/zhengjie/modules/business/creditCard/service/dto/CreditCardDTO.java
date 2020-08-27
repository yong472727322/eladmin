package me.zhengjie.modules.business.creditCard.service.dto;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
* @author leo
* @date 2019-12-25
*/
@Data
public class CreditCardDTO implements Serializable {

    private Long id;

    // 信用卡名称
    private String cardName;

    // 信用卡号
    private String cardNumber;

    // 到期时间
    private String cardExp;

    // 过期时间
    private Date cardExpDate;

    // 校验码
    private String cardCvv;

    // 当前金额
    private BigDecimal currentAmount;

    // 币种
    private String currency;

    // Label
    private String cardLabel;

    // 过期时间
    private Date closeDate;

    // 信用卡状态：0-不可用，1-可用
    private Integer cardStatus;

    // 数据状态：0-无效，1-有效
    private Integer dataStatus;

    // 创建人
    private String createdBy;

    // 创建时间
    private Date createdDate;
}