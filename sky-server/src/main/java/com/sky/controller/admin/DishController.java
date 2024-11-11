package com.sky.controller.admin;

import com.sky.dto.DishDTO;
import com.sky.dto.DishPageQueryDTO;
import com.sky.entity.Dish;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.DishService;
import com.sky.vo.DishVO;
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
        log.info("批量删除菜品，参数：{}", ids);
        dishService.deleteBatch(ids);
        return Result.success();
    }

    /**
     * 根据id查询菜品
     * @param id
     * @return
     */
    @ApiOperation("根据id查询菜品")
    @GetMapping("/{id}")
    public Result<DishVO> getByIdWithFlavor(@PathVariable Long id) {
        log.info("根据id查询菜品，参数：{}", id);
        DishVO dishVO = dishService.getByIdWithFlavor(id);
        return Result.success(dishVO);
    }

    /**
     * 修改菜品
     * @param dishDTO
     * @return
     */
    @ApiOperation("修改菜品")
    @PutMapping
    public Result updateDishWithFlavor(@RequestBody DishDTO dishDTO) {
        log.info("修改菜品，参数：{}", dishDTO);
        dishService.updateDishWithFlavor(dishDTO);
        return Result.success();
    }

    /**
     * 启用禁用菜品
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用禁用菜品")
    @PostMapping("/status/{status}")
    public Result updateStatusWithSeatmeal(@PathVariable Integer status, Long id) {
    	log.info("启用禁用菜品，参数：{}", status);
    	dishService.updateStatusWithSeatmeal(status, id);
    	return Result.success();
    }

    /**
     * 根据分类id查询菜品
     * @param categoryId
     * @return
     */
    @ApiOperation("根据分类id查询菜品")
    @GetMapping("/list")
    public Result<List<Dish>> getDishByCategoryId(Long categoryId) {
        log.info("根据分类id查询菜品，参数：{}", categoryId);
        List<Dish> dishList = dishService.getDishByCategoryId(categoryId);
        return Result.success(dishList);
    }
}
