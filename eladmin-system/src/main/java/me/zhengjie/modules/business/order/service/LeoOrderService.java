package me.zhengjie.modules.business.order.service;

import me.zhengjie.modules.business.order.domain.LeoOrder;
import me.zhengjie.modules.business.order.service.dto.LeoOrderDTO;
import me.zhengjie.modules.business.order.service.dto.LeoOrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import java.util.Map;
import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

/**
* @author leo
* @date 2019-12-19
*/
public interface LeoOrderService {

    /**
    * 查询数据分页
    * @param criteria 条件参数
    * @param pageable 分页参数
    * @return Map<String,Object>
    */
    Map<String,Object> queryAll(LeoOrderQueryCriteria criteria, Pageable pageable);

    /**
    * 查询所有数据不分页
    * @param criteria 条件参数
    * @return List<LeoOrderDTO>
    */
    List<LeoOrderDTO> queryAll(LeoOrderQueryCriteria criteria);

    /**
     * 根据ID查询
     * @param id ID
     * @return LeoOrderDTO
     */
    LeoOrderDTO findById(Long id);

    LeoOrderDTO create(LeoOrder resources);

    void update(LeoOrder resources);

    void delete(Long id);

    void download(List<LeoOrderDTO> all, HttpServletResponse response) throws IOException;
}