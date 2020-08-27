package me.zhengjie.modules.business.order.domain;

import lombok.Data;
import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import javax.persistence.*;
import java.io.Serializable;

/**
* @author leo
* @date 2019-12-19
*/
@Entity
@Data
@Table(name="leo_order")
public class LeoOrder implements Serializable {

    // ID
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // 订单名称
    @Column(name = "name")
    private String name;

    public void copy(LeoOrder source){
        BeanUtil.copyProperties(source,this, CopyOptions.create().setIgnoreNullValue(true));
    }
}