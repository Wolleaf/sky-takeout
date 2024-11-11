package com.sky.controller.admin;

import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 分类管理
 */
@Api(tags = "菜品分类相关接口")
@Slf4j
@RestController
@RequestMapping("admin/category")
public class CategoryController {

    private CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * 根据菜品类型查询分类
     * @param type
     * @return
     */
    @ApiOperation("根据菜品类型查询分类")
    @GetMapping("/list")
    public Result<List<Category>> getByType(Integer type) {
        log.info("根据菜品类型查询分类: {}", type);
        List<Category> list = categoryService.getByType(type);
        return Result.success(list);
    }

    /**
     * 新增分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("新增分类")
    @PostMapping
    public Result addCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("新增分类，分类数据: {}", categoryDTO);
        categoryService.addCategory(categoryDTO);
        return Result.success();
    }

    /**
     * 启用禁用分类
     * @param status
     * @param id
     * @return
     */
    @ApiOperation("启用禁用分类")
    @PostMapping("/status/{status}")
    public Result updateStatus(@PathVariable Integer status, Long id) {
        log.info("启用禁用分类，状态为：{}, 分类id为：{}", status, id);
        categoryService.updateStatus(status, id);
    	return Result.success();
    }

    /**
     * 修改分类
     * @param categoryDTO
     * @return
     */
    @ApiOperation("修改分类")
    @PutMapping
    public Result updateCategory(@RequestBody CategoryDTO categoryDTO) {
        log.info("修改分类，分类数据: {}", categoryDTO);
    	categoryService.updateCategory(categoryDTO);
    	return Result.success();
    }

    /**
     * 分页查询分类
     * @param categoryPageQueryDTO
     * @return
     */
    @ApiOperation("分页查询分类")
    @GetMapping("/page")
    public Result<PageResult> queryPage(CategoryPageQueryDTO categoryPageQueryDTO) {
        log.info("分页查询分类，参数为：{}", categoryPageQueryDTO);
        PageResult pageResult = categoryService.queryPage(categoryPageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     * 删除分类
     * @param id
     * @return
     */
    @ApiOperation("删除分类")
    @DeleteMapping
    public Result deleteById(Long id) {
        categoryService.deleteById(id);
        return Result.success();
    }
}
