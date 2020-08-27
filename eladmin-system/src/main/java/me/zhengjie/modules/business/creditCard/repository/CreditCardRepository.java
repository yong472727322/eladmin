package me.zhengjie.modules.business.creditCard.repository;

import me.zhengjie.modules.business.creditCard.domain.CreditCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
* @author leo
* @date 2019-12-25
*/
public interface CreditCardRepository extends JpaRepository<CreditCard, Long>, JpaSpecificationExecutor<CreditCard> {
}