package me.zhengjie.modules.business.order.repository;

import me.zhengjie.modules.business.order.domain.LeoOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author leo
* @date 2019-12-19
*/
public interface LeoOrderRepository extends JpaRepository<LeoOrder, Long>, JpaSpecificationExecutor<LeoOrder> {
}