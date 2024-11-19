package com.sky.service.impl;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private OrderMapper orderMapper;

    @Autowired
    public ReportServiceImpl(OrderMapper orderMapper) {
        this.orderMapper = orderMapper;
    }

    /**
     * 营业额统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        List<TurnoverStatisticsDTO> list = orderMapper.getTurnoverStatistics(begin, end, Orders.COMPLETED);
        StringBuilder date = new StringBuilder();
        StringBuilder turnover = new StringBuilder();
        for (TurnoverStatisticsDTO dto : list) {
            date.append(dto.getDate()).append(",");
            turnover.append(dto.getTurnover()).append(",");
        }
        date.deleteCharAt(date.length() - 1);
        turnover.deleteCharAt(turnover.length() - 1);
        return new TurnoverReportVO(date.toString(), turnover.toString());
    }

    /**
     * 用户统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        List<NewUserStatisticsDTO> newUserStatisticsDTOList = orderMapper.getNewUserStatistics(begin, end);
        List<TotalUserStatisticsDTO> totalUserStatisticsDTOList = orderMapper.getTotalUserStatistics(begin, end);
        StringBuilder date = new StringBuilder();
        StringBuilder newUser = new StringBuilder();
        StringBuilder totalUser = new StringBuilder();
        for (int i = 0; i < newUserStatisticsDTOList.size(); i++) {
            NewUserStatisticsDTO newUserStatisticsDTO = newUserStatisticsDTOList.get(i);
            TotalUserStatisticsDTO totalUserStatisticsDTO = totalUserStatisticsDTOList.get(i);
            date.append(newUserStatisticsDTO.getDate()).append(",");
            newUser.append(newUserStatisticsDTO.getTotal()).append(",");
            totalUser.append(totalUserStatisticsDTO.getTotal()).append(",");
        }
        date.deleteCharAt(date.length() - 1);
        newUser.deleteCharAt(newUser.length() - 1);
        totalUser.deleteCharAt(totalUser.length() - 1);
        return UserReportVO.builder()
                .dateList(date.toString())
                .newUserList(newUser.toString())
                .totalUserList(totalUser.toString())
                .build();
    }

    /**
     * 订单统计
     * @param begin
     * @param end
     * @return
     */
    @Override
    public OrderReportVO orderStatistics(LocalDate begin, LocalDate end) {
        Integer totalOrderCount = orderMapper.countAll();
        Integer validOrderCount = orderMapper.countByStatus(Orders.COMPLETED);
        Double orderCompletionRate = validOrderCount / (double) totalOrderCount;
        List<Integer> order = orderMapper.getOrderCountList(begin, end, null);
        List<Integer> validOrder = orderMapper.getOrderCountList(begin, end, Orders.COMPLETED);
        StringBuilder dateList = new StringBuilder();
        StringBuilder orderCountList = new StringBuilder();
        StringBuilder validOrderCountList = new StringBuilder();
        int i = 0;
        while (begin.isBefore(end)) {
            dateList.append(begin).append(",");
            orderCountList.append(order.get(i)).append(",");
            validOrderCountList.append(validOrder.get(i)).append(",");
            ++i;
            begin = begin.plusDays(1);
        }
        dateList.append(end);
        orderCountList.append(order.get(i));
        validOrderCountList.append(validOrder.get(i));
        return OrderReportVO.builder()
                .dateList(dateList.toString())
                .orderCountList(orderCountList.toString())
                .validOrderCountList(validOrderCountList.toString())
                .totalOrderCount(totalOrderCount)
                .validOrderCount(validOrderCount)
                .orderCompletionRate(orderCompletionRate).build();
    }

    /**
     * 销量排名前10
     * @param begin
     * @param end
     * @return
     */
    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {
        List<GoodsSalesDTO> list = orderMapper.getTop10(begin, end);
        StringBuilder nameList = new StringBuilder();
        StringBuilder numberList = new StringBuilder();
        for (GoodsSalesDTO dto : list) {
            nameList.append(dto.getName()).append(",");
            numberList.append(dto.getNumber()).append(",");
        }
        nameList.deleteCharAt(nameList.length() - 1);
        numberList.deleteCharAt(numberList.length() - 1);
        return SalesTop10ReportVO.builder()
                .nameList(nameList.toString())
                .numberList(numberList.toString())
                .build();
    }
}
