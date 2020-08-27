package me.zhengjie.modules.business.order.service.impl;

import me.zhengjie.modules.business.order.domain.LeoOrder;
import me.zhengjie.utils.ValidationUtil;
import me.zhengjie.utils.FileUtil;
import me.zhengjie.modules.business.order.repository.LeoOrderRepository;
import me.zhengjie.modules.business.order.service.LeoOrderService;
import me.zhengjie.modules.business.order.service.dto.LeoOrderDTO;
import me.zhengjie.modules.business.order.service.dto.LeoOrderQueryCriteria;
import me.zhengjie.modules.business.order.service.mapper.LeoOrderMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import me.zhengjie.utils.PageUtil;
import me.zhengjie.utils.QueryHelp;
import java.util.List;
import java.util.Map;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
* @author leo
* @date 2019-12-19
*/
@Service
@CacheConfig(cacheNames = "leoOrder")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class LeoOrderServiceImpl implements LeoOrderService {

    private final LeoOrderRepository leoOrderRepository;

    private final LeoOrderMapper leoOrderMapper;

    public LeoOrderServiceImpl(LeoOrderRepository leoOrderRepository, LeoOrderMapper leoOrderMapper) {
        this.leoOrderRepository = leoOrderRepository;
        this.leoOrderMapper = leoOrderMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(LeoOrderQueryCriteria criteria, Pageable pageable){
        Page<LeoOrder> page = leoOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(leoOrderMapper::toDto));
    }

    @Override
    @Cacheable
    public List<LeoOrderDTO> queryAll(LeoOrderQueryCriteria criteria){
        return leoOrderMapper.toDto(leoOrderRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public LeoOrderDTO findById(Long id) {
        LeoOrder leoOrder = leoOrderRepository.findById(id).orElseGet(LeoOrder::new);
        ValidationUtil.isNull(leoOrder.getId(),"LeoOrder","id",id);
        return leoOrderMapper.toDto(leoOrder);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public LeoOrderDTO create(LeoOrder resources) {
        return leoOrderMapper.toDto(leoOrderRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(LeoOrder resources) {
        LeoOrder leoOrder = leoOrderRepository.findById(resources.getId()).orElseGet(LeoOrder::new);
        ValidationUtil.isNull( leoOrder.getId(),"LeoOrder","id",resources.getId());
        leoOrder.copy(resources);
        leoOrderRepository.save(leoOrder);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        leoOrderRepository.deleteById(id);
    }


    @Override
    public void download(List<LeoOrderDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (LeoOrderDTO leoOrder : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("订单名称", leoOrder.getName());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }
}