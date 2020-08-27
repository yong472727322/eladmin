package me.zhengjie.modules.business.order.rest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.aop.log.Log;
import me.zhengjie.modules.business.order.domain.LeoOrder;
import me.zhengjie.modules.business.order.service.LeoOrderService;
import me.zhengjie.modules.business.order.service.dto.LeoOrderQueryCriteria;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
* @author leo
* @date 2019-12-19
*/
@Api(tags = "LeoOrder管理")
@RestController
@RequestMapping("/api/leoOrder")
public class LeoOrderController {

    private final LeoOrderService leoOrderService;

    public LeoOrderController(LeoOrderService leoOrderService) {
        this.leoOrderService = leoOrderService;
    }

    @Log("导出数据")
    @ApiOperation("导出数据")
    @GetMapping(value = "/download")
    @PreAuthorize("@el.check('leoOrder:list')")
    public void download(HttpServletResponse response, LeoOrderQueryCriteria criteria) throws IOException {
        leoOrderService.download(leoOrderService.queryAll(criteria), response);
    }

    @GetMapping
    @Log("查询LeoOrder")
    @ApiOperation("查询LeoOrder")
    @PreAuthorize("@el.check('leoOrder:list')")
    public ResponseEntity getLeoOrders(LeoOrderQueryCriteria criteria, Pageable pageable){
        Map<String, Object> map = leoOrderService.queryAll(criteria, pageable);
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping
    @Log("新增LeoOrder")
    @ApiOperation("新增LeoOrder")
    @PreAuthorize("@el.check('leoOrder:add')")
    public ResponseEntity create(@Validated @RequestBody LeoOrder resources){
        return new ResponseEntity<>(leoOrderService.create(resources),HttpStatus.CREATED);
    }

    @PutMapping
    @Log("修改LeoOrder")
    @ApiOperation("修改LeoOrder")
    @PreAuthorize("@el.check('leoOrder:edit')")
    public ResponseEntity update(@Validated @RequestBody LeoOrder resources){
        leoOrderService.update(resources);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(value = "/{id}")
    @Log("删除LeoOrder")
    @ApiOperation("删除LeoOrder")
    @PreAuthorize("@el.check('leoOrder:del')")
    public ResponseEntity delete(@PathVariable Long id){
        leoOrderService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }
}