package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 菜品相关接口
 */
@Slf4j
@Api(tags = "菜品相关接口")
@RestController
@RequestMapping("/admin/dish")
public class DishController {

    private DishService dishService;

    @Autowired
    public DishController(DishService dishService) {
        this.dishService = dishService;
    }

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     * @return
     */
    @ApiOperation("新增菜品")
    @PostMapping
    public Result addDish(@RequestBody DishDTO dishDTO) {
        log.info("新增菜品，参数：{}", dishDTO);
        dishService.addDishWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 分页查询菜品
     * @param dishPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询菜品")
    @GetMapping("/page")
    public Result<PageResult> queryPage(DishPageQueryDTO dishPageQueryDTO) {
        log.info("分页查询菜品，参数：{}", dishPageQueryDTO);
        PageResult pageResult = dishService.queryPage(dishPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 批量删除菜品
     * @param ids
     * @return
     */
    @ApiOperation("批量删除菜品")
    @DeleteMapping
    public Result deleteBatch(@RequestParam List<Long> ids) {
        dishService.deleteBatch(ids);
        return Result.success();
    }
}
