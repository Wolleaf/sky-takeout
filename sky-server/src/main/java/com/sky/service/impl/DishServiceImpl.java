package com.sky.service.impl;

import com.sky.constant.StatusConstant;
import com.sky.dto.DishDTO;
import com.sky.entity.Dish;
import com.sky.entity.DishFlavor;
import com.sky.mapper.DishFlavorMapper;
import com.sky.mapper.DishMapper;
import com.sky.service.DishService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 菜品管理
 */
@Service
public class DishServiceImpl implements DishService {

    private DishMapper dishMapper;
    private DishFlavorMapper dishFlavorMapper;

    @Autowired
    public DishServiceImpl(DishMapper dishMapper, DishFlavorMapper dishFlavorMapper) {
        this.dishMapper = dishMapper;
        this.dishFlavorMapper = dishFlavorMapper;
    }

    /**
     * 新增菜品和批量插入对应菜品口味数据
     * @param dishDTO
     */
    @Override
    @Transactional
    public void addDishWithFlavor(DishDTO dishDTO) {
        // 插入菜品
        Dish dish = new Dish();
        dish.setStatus(StatusConstant.DISABLE);
        BeanUtils.copyProperties(dishDTO, dish);
        dishMapper.insertDish(dish);

        // 插入口味
        List<DishFlavor> flavors = dishDTO.getFlavors();
        if (flavors != null && flavors.size() > 0) {
            flavors.forEach(dishFlavor -> dishFlavor.setDishId(dish.getId()));
            dishFlavorMapper.insertBatch(dishDTO.getFlavors());
        }
    }
}
