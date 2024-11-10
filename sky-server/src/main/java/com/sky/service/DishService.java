package com.sky.service;

import com.sky.dto.DishDTO;

/**
 * 菜品管理
 */
public interface DishService {

    /**
     * 新增菜品和对应口味
     * @param dishDTO
     */
    void addDishWithFlavor(DishDTO dishDTO);
}
