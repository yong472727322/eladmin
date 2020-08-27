package me.zhengjie.modules.business.creditCard.service;

import me.zhengjie.modules.business.creditCard.domain.CreditCard;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardDTO;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
* @author leo
* @date 2019-12-25
*/
public interface CreditCardService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(CreditCardQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<CreditCardDTO>
    */
    List<CreditCardDTO> queryAll(CreditCardQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return CreditCardDTO
     */
    CreditCardDTO findById(Long id);

    CreditCardDTO create(CreditCard resources);

    void update(CreditCard resources);

    void delete(Long id);

    void download(List<CreditCardDTO> all, HttpServletResponse response) throws IOException;

    /**
     * 上传信用卡
     * @param file
     * @return
     */
    CreditCardDTO upload(MultipartFile file);
}