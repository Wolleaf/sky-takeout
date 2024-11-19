package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.*;
import com.sky.entity.Orders;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Mapper
public interface OrderMapper {

    /**
     * 保存订单数据
     * @param orders
     */
    void insert(Orders orders);

    /**
     * 根据订单号查询订单
     * @param orderNumber
     */
    @Select("select * from orders where number = #{orderNumber}")
    Orders getByNumber(String orderNumber);

    /**
     * 修改订单信息
     * @param orders
     */
    void update(Orders orders);

    /**
     * 分页查询订单
     * @param ordersPageQueryDTO
     * @return
     */
    Page<Orders> pageQuery(OrdersPageQueryDTO ordersPageQueryDTO);

    /**
     * 根据id查询订单
     * @param id
     * @return
     */
    @Select("select * from orders where id = #{id}")
    Orders getById(Long id);

    /**
     * 取消订单
     * @param id
     */
    @Update("update orders set status = 6 where id = #{id}")
    void cancelById(Long id);

    /**
     * 根据状态统计订单数量
     * @param status
     * @return
     */
    @Select("select count(*) from orders where status = #{status}")
    Integer countByStatus(Integer status);

    /**
     * 根据状态和小于时间查询订单
     * @param status
     * @param time
     * @return
     */
    @Select("select * from orders where status = #{status} and order_time < #{time}")
    List<Orders> getByStatusAndLTTime(Integer status, LocalDateTime time);

    /**
     * 根据时间和状态统计营业额
     *
     * @param begin
     * @param end
     * @param status
     * @return
     */
    List<TurnoverStatisticsDTO> getTurnoverStatistics(LocalDate begin, LocalDate end, Integer status);

    /**
     * 根据时间统计用户数量
     * @param begin
     * @param end
     * @return
     */
    List<TotalUserStatisticsDTO> getTotalUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 根据时间统计新增用户数量
     * @param begin
     * @param end
     * @return
     */
    List<NewUserStatisticsDTO> getNewUserStatistics(LocalDate begin, LocalDate end);

    /**
     * 统计总订单数量
     * @return
     */
    @Select("select count(*) from orders")
    Integer countAll();

    /**
     * 统计一段时期内订单数量
     * @param begin
     * @param end
     * @param status
     * @return
     */
    List<Integer> getOrderCountList(LocalDate begin, LocalDate end, Integer status);

    /**
     * 查询销量排名top10
     *
     * @param begin
     * @param end
     * @return
     */
    List<GoodsSalesDTO> getTop10(LocalDate begin, LocalDate end);

    /**
     * 根据动态条件统计营业额数据
     * @param map
     * @return
     */
    Double sumByMap(Map map);

    /**
     * 根据动态条件统计订单数量
     * @param map
     * @return
     */
    Integer countByMap(Map map);

}
