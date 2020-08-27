package me.zhengjie.modules.business.creditCard.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.math.BigDecimal;
import java.io.Serializable;
import java.util.Date;

/**
* @author leo
* @date 2019-12-25
*/
@Entity
@Data
@Table(name="credit_card")
public class CreditCard implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 信用卡名称
    @Column(name = "card_name")
    private String cardName;

    // 信用卡号
    @Column(name = "card_number")
    private String cardNumber;

    // 到期时间
    @Column(name = "card_exp")
    private String cardExp;

    // 过期时间
    @Column(name = "card_exp_date")
    private Date cardExpDate;

    // 校验码
    @Column(name = "card_cvv")
    private String cardCvv;

    // 当前金额
    @Column(name = "current_amount")
    private BigDecimal currentAmount;

    // 币种
    @Column(name = "currency")
    private String currency;

    // Label
    @Column(name = "card_label")
    private String cardLabel;

    // 过期时间
    @Column(name = "close_date")
    private Date closeDate;

    // 信用卡状态：0-不可用，1-可用
    @Column(name = "card_status")
    private Integer cardStatus;

    // 数据状态：0-无效，1-有效
    @Column(name = "data_status")
    private Integer dataStatus;

    // 创建人
    @Column(name = "created_by")
    private String createdBy;

    // 创建时间
    @Column(name = "created_date")
    private Date createdDate;

    public void copy(CreditCard source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}