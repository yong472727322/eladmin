package me.zhengjie.modules.business.creditCard.service.impl;

import cn.hutool.core.util.ObjectUtil;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.business.creditCard.domain.CreditCard;
import me.zhengjie.modules.business.creditCard.repository.CreditCardRepository;
import me.zhengjie.modules.business.creditCard.service.CreditCardService;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardDTO;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardQueryCriteria;
import me.zhengjie.modules.business.creditCard.service.mapper.CreditCardMapper;
import me.zhengjie.utils.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
* @author leo
* @date 2019-12-25
*/
@Service
@CacheConfig(cacheNames = "creditCard")
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true, rollbackFor = Exception.class)
public class CreditCardServiceImpl implements CreditCardService {

    private final CreditCardRepository creditCardRepository;

    private final CreditCardMapper creditCardMapper;

    @Value("${file.path}")
    private String path;

    @Value("${file.maxSize}")
    private long maxSize;

    public CreditCardServiceImpl(CreditCardRepository creditCardRepository, CreditCardMapper creditCardMapper) {
        this.creditCardRepository = creditCardRepository;
        this.creditCardMapper = creditCardMapper;
    }

    @Override
    @Cacheable
    public Map<String,Object> queryAll(CreditCardQueryCriteria criteria, Pageable pageable){
        Page<CreditCard> page = creditCardRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder),pageable);
        return PageUtil.toPage(page.map(creditCardMapper::toDto));
    }

    @Override
    @Cacheable
    public List<CreditCardDTO> queryAll(CreditCardQueryCriteria criteria){
        return creditCardMapper.toDto(creditCardRepository.findAll((root, criteriaQuery, criteriaBuilder) -> QueryHelp.getPredicate(root,criteria,criteriaBuilder)));
    }

    @Override
    @Cacheable(key = "#p0")
    public CreditCardDTO findById(Long id) {
        CreditCard creditCard = creditCardRepository.findById(id).orElseGet(CreditCard::new);
        ValidationUtil.isNull(creditCard.getId(),"CreditCard","id",id);
        return creditCardMapper.toDto(creditCard);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public CreditCardDTO create(CreditCard resources) {
        return creditCardMapper.toDto(creditCardRepository.save(resources));
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void update(CreditCard resources) {
        CreditCard creditCard = creditCardRepository.findById(resources.getId()).orElseGet(CreditCard::new);
        ValidationUtil.isNull( creditCard.getId(),"CreditCard","id",resources.getId());
        creditCard.copy(resources);
        creditCardRepository.save(creditCard);
    }

    @Override
    @CacheEvict(allEntries = true)
    @Transactional(rollbackFor = Exception.class)
    public void delete(Long id) {
        creditCardRepository.deleteById(id);
    }


    @Override
    public void download(List<CreditCardDTO> all, HttpServletResponse response) throws IOException {
        List<Map<String, Object>> list = new ArrayList<>();
        for (CreditCardDTO creditCard : all) {
            Map<String,Object> map = new LinkedHashMap<>();
            map.put("Label", creditCard.getCardLabel());
            map.put("信用卡名称", creditCard.getCardName());
            map.put("信用卡号", creditCard.getCardNumber());
            map.put("到期时间", creditCard.getCardExp());
            map.put("过期时间", creditCard.getCardExpDate());
            map.put("校验码", creditCard.getCardCvv());
            map.put("当前金额", creditCard.getCurrentAmount());
            map.put("过期时间", creditCard.getCloseDate());
            map.put("信用卡状态：0-不可用，1-可用", creditCard.getCardStatus());
//            map.put("数据状态：0-无效，1-有效", creditCard.getDataStatus());
            map.put("创建人", creditCard.getCreatedBy());
            map.put("创建时间", creditCard.getCreatedDate());
            list.add(map);
        }
        FileUtil.downloadExcel(list, response);
    }

    @Override
    public CreditCardDTO upload(MultipartFile multipartFile) {
        FileUtil.checkSize(maxSize, multipartFile.getSize());
        String suffix = FileUtil.getExtensionName(multipartFile.getOriginalFilename());
        String type = FileUtil.getFileType(suffix);
        File file = FileUtil.upload(multipartFile, path + type +  File.separator);
        if(ObjectUtil.isNull(file)){
            throw new BadRequestException("上传失败");
        }

        // 上传成功，处理数据
        List<Map<String, String>> list = ExcelUtil.readExcel(file.getAbsolutePath());
        if(null != list){
            for (Map<String, String> map : list) {
//                Label	Card name	Card number	Card EXP	CVV	Amount
                String label = map.get("Label");
                String cardName = map.get("Card name");
                String cardNumber = map.get("Card number");
                String exp = map.get("Card EXP");
                String cvv = map.get("CVV");
                String amount = map.get("Amount");

                CreditCard creditCard = new CreditCard();
                creditCard.setCardLabel(label);
                creditCard.setCardName(cardName);
                creditCard.setCardNumber(cardNumber);
                creditCard.setCardExp(exp);
                creditCard.setCardCvv(cvv);
                creditCard.setCurrency("$");
                // 设置当前金额
                BigDecimal currentAmount;
                try{
                    currentAmount = new BigDecimal(amount);
                }catch (NumberFormatException e){
                    creditCard.setCurrency(amount.substring(0, 1));
                    currentAmount = new BigDecimal(amount.substring(1, amount.length()));
                }
                creditCard.setCurrentAmount(currentAmount);

                // 计算过期日期
                creditCard.setCardExpDate(DateUtil.caculateExpireDate(exp));
                creditCard.setCardStatus(1);
                creditCard.setDataStatus(1);
                creditCard.setCreatedDate(DateUtil.getNow());

                String username = SecurityUtils.getUsername();
                creditCard.setCreatedBy(username);
                this.create(creditCard);
            }
        }
        return new CreditCardDTO();
    }
}