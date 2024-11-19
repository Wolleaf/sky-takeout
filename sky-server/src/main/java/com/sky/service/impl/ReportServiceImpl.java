package com.sky.service.impl;

import com.sky.dto.*;
import com.sky.entity.Orders;
import com.sky.mapper.OrderMapper;
import com.sky.service.ReportService;
import com.sky.service.WorkspaceService;
import com.sky.vo.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
public class ReportServiceImpl implements ReportService {

    private OrderMapper orderMapper;
    private WorkspaceService workspaceService;

    @Autowired
    public ReportServiceImpl(OrderMapper orderMapper, WorkspaceService workspaceService) {
        this.orderMapper = orderMapper;
        this.workspaceService = workspaceService;
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

    /**
     * 导出运营数据报表
     * @param response
     */
    @Override
    public void exportBusinessData(HttpServletResponse response) {
        try (
                InputStream in = this.getClass().getResourceAsStream("/template/运营数据报表模板.xlsx");
                XSSFWorkbook workbook = new XSSFWorkbook(in);
                ServletOutputStream outputStream = response.getOutputStream();
        )
        {
            // 查询概览数据
            LocalDate begin = LocalDate.now().minusDays(30);
            LocalDate end = LocalDate.now().minusDays(1);
            BusinessDataVO businessData = workspaceService.getBusinessData(
                    LocalDateTime.of(begin, LocalTime.MIN), LocalDateTime.of(end, LocalTime.MAX)
            );
            XSSFSheet sheet1 = workbook.getSheet("Sheet1");

            XSSFRow row2 = sheet1.getRow(1);
            row2.getCell(1).setCellValue(begin + "至" + end);

            XSSFRow row4 = sheet1.getRow(3);
            row4.getCell(2).setCellValue(businessData.getTurnover());
            row4.getCell(4).setCellValue(businessData.getOrderCompletionRate());
            row4.getCell(6).setCellValue(businessData.getNewUsers());
            XSSFRow row5 = sheet1.getRow(4);
            row5.getCell(2).setCellValue(businessData.getValidOrderCount());
            row5.getCell(4).setCellValue(businessData.getUnitPrice());

            // 填充明细数据
            for (int i = 0; i < 30; ++i) {
                LocalDate date = begin.plusDays(i);
                BusinessDataVO businessDataOnDate = workspaceService.getBusinessData(
                        LocalDateTime.of(date, LocalTime.MIN), LocalDateTime.of(date, LocalTime.MAX)
                );
                int rowNumber = 7 + i;
                XSSFRow row = sheet1.getRow(rowNumber);
                row.getCell(1).setCellValue(date.toString());
                row.getCell(2).setCellValue(businessDataOnDate.getTurnover());
                row.getCell(3).setCellValue(businessDataOnDate.getValidOrderCount());
                row.getCell(4).setCellValue(businessDataOnDate.getOrderCompletionRate());
                row.getCell(5).setCellValue(businessDataOnDate.getUnitPrice());
                row.getCell(6).setCellValue(businessDataOnDate.getNewUsers());
            }

            workbook.write(outputStream);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
}
