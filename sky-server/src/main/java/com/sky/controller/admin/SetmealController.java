package com.sky.controller.admin;

import com.sky.dto.SetmealDTO;
import com.sky.result.Result;
import com.sky.service.SetmealService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 套餐相关接口
 */
@Slf4j
@Api(tags = "套餐相关接口")
@RestController
@RequestMapping("/admin/setmeal")
public class SetmealController {

    private SetmealService setmealService;

    @Autowired
    public SetmealController(SetmealService setmealService) {
        this.setmealService = setmealService;
    }

    /**
     * 新增套餐
     * @param setmealDTO
     * @return
     */
    @ApiOperation("新增套餐")
    @PostMapping
    public Result addSetmeal(@RequestBody SetmealDTO setmealDTO) {
        setmealService.addSetmeal(setmealDTO);
        return Result.success();
    }
}
