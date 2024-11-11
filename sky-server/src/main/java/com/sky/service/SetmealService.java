package com.sky.service;

import com.sky.dto.SetmealDTO;

/**
 * 套餐业务
 */
public interface SetmealService {

    /**
     * 新增套餐
     * @param setmealDTO
     */
    void addSetmeal(SetmealDTO setmealDTO);
}