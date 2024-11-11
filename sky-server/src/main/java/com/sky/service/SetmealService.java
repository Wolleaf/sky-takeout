package com.sky.service;

import com.sky.dto.SetmealDTO;
import com.sky.dto.SetmealPageQueryDTO;
import com.sky.result.PageResult;
import com.sky.vo.SetmealVO;

import java.util.List;

/**
 * 套餐业务
 */
public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);

    /**
     * 分页查询套餐
     * @param setmealPageQueryDTO
     * @return
     */
    PageResult queryPage(SetmealPageQueryDTO setmealPageQueryDTO);

    /**
     * 批量删除套餐
     * @param ids
     */
    void deleteBatch(List<Long> ids);

    /**
     * 根据id查询套餐
     * @param id
     * @return
     */
    SetmealVO getSetmealByIdWithDishes(Long id);

    /**
     * 修改套餐
     * @param setmealDTO
     */
    void updateSetmealWithDishes(SetmealDTO setmealDTO);

    /**
     * 修改套餐起售停售
     * @param status
     * @param id
     */
    void updateStatus(Integer status, Long id);
}
