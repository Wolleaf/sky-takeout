package com.sky.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface SetmealDishMapper {

    /**
     * 根据菜品id查询套餐数量
     * @param id
     * @return
     */
    @Select("select count(*) from setmeal_dish where dish_id = #{dishId}")
    Integer countByDishId(Long dishId);
}
