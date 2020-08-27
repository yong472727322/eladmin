package me.zhengjie.modules.business.creditCard.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.business.creditCard.domain.CreditCard;
import me.zhengjie.modules.business.creditCard.service.CreditCardService;
import me.zhengjie.modules.business.creditCard.service.dto.CreditCardQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author leo
* @date 2019-12-25
*/
@RestController
@Api(tags = "CreditCard管理")
@RequestMapping("/api/creditCard")
public class CreditCardController {

    private final CreditCardService creditCardService;

    public CreditCardController(CreditCardService creditCardService) {
        this.creditCardService = creditCardService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('creditCard:list')")
    public void download(HttpServletResponse response, CreditCardQueryCriteria criteria) throws IOException {
        creditCardService.download(creditCardService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询CreditCard")
    @ApiOperation("查询CreditCard")
    @PreAuthorize("@el.check('creditCard:list')")
    public ResponseEntity getCreditCards(CreditCardQueryCriteria criteria, Pageable pageable){
        return new ResponseEntity<>(creditCardService.queryAll(criteria,pageable),HttpStatus.OK);
    }

    @PostMapping
    @Log("新增CreditCard")
    @ApiOperation("新增CreditCard")
    @PreAuthorize("@el.check('creditCard:add')")
    public ResponseEntity create(@Validated @RequestBody CreditCard resources){

        return new ResponseEntity<>(creditCardService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改CreditCard")
    @ApiOperation("修改CreditCard")
    @PreAuthorize("@el.check('creditCard:edit')")
    public ResponseEntity update(@Validated @RequestBody CreditCard resources){
        creditCardService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除CreditCard")
    @ApiOperation("删除CreditCard")
    @PreAuthorize("@el.check('creditCard:del')")
    public ResponseEntity delete(@PathVariable Long id){
        creditCardService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @PostMapping("upload")
    @ApiOperation("上传信用卡")
    @PreAuthorize("@el.check('creditCard:add')")
    public ResponseEntity upload(@RequestParam("file") MultipartFile file){
        creditCardService.upload(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

}