import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.controller.OmsOrderController;
import com.macro.mall.dto.OmsOrderDeliveryParam;
import com.macro.mall.mapper.OmsOrderMapper;
import com.macro.mall.service.OmsOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import com.macro.mall.dao.OmsOrderDao;
import com.macro.mall.model.OmsOrder;


@SpringBootTest(classes = com.macro.mall.MallAdminApplication.class)
@Transactional
public class OmsOrderServiceFsmTest {
    @Autowired
    // use the true realization.
    private OmsOrderService orderService;

    @Autowired
    private OmsOrderDao orderDao;

    @Autowired
    private OmsOrderMapper orderMapper;

    private OmsOrder createTestOrder() {
        OmsOrder order = new OmsOrder();
        order.setId(14L);                              // Assign ID
        order.setMemberId(1L);                         // member_id
        order.setCouponId(2L);                         // coupon_id
        order.setOrderSn("201809130101000001");       // order_sn
        order.setCreateTime(new Date());               // create_time
        order.setMemberUsername("test");              // member_username
        order.setTotalAmount(new BigDecimal("18732.00"));   // total_amount
        order.setPayAmount(new BigDecimal("16377.75"));     // pay_amount
        order.setFreightAmount(new BigDecimal("0.00"));     // freight_amount
        order.setPromotionAmount(new BigDecimal("2344.25"));// promotion_amount
        order.setIntegrationAmount(new BigDecimal("0.00")); // integration_amount
        order.setCouponAmount(new BigDecimal("10.00"));     // coupon_amount
        order.setDiscountAmount(new BigDecimal("0.00"));    // discount_amount
        order.setPayType(2);                           // pay_type
        order.setSourceType(1);                        // source_type
        order.setStatus(3);                            // status 当前状态，例如已付款
        order.setOrderType(0);                         // order_type
        order.setDeliveryCompany("顺丰快递");         // delivery_company
        order.setDeliverySn("201707196398345");       // delivery_sn
        order.setAutoConfirmDay(15);                   // auto_confirm_day
        order.setIntegration(13284);                   // integration
        order.setGrowth(13284);                        // growth
        order.setPromotionInfo("单品促销,打折优惠：满3件，打7.50折,满减优惠：满1000.00元，减120.00元,满减优惠：满1000.00元，减120.00元,无优惠"); // promotion_info
        order.setReceiverName("大梨");                 // receiver_name
        order.setReceiverPhone("18033441849");        // receiver_phone
        order.setReceiverPostCode("518000");          // receiver_post_code
        order.setReceiverProvince("广东省");           // receiver_province
        order.setReceiverCity("深圳市");               // receiver_city
        order.setReceiverRegion("福田区");             // receiver_region
        order.setReceiverDetailAddress("东晓街道");    // receiver_detail_address
        order.setDeleteStatus(0);                      // delete_status
        order.setConfirmStatus(1);                     // confirm_status
        order.setUseIntegration(0);                    // use_integration
        Date now = new Date();
        order.setPaymentTime(now);
        order.setDeliveryTime(now);
        order.setReceiveTime(now);
        order.setCommentTime(now);
        order.setModifyTime(now);
        // other necessary fields
        orderMapper.insertSelective(order);
        return order;
    }


    @Test
    public void testDeliveryChangesStatusSuccessful() {
        OmsOrder order = createTestOrder();
        OmsOrderDeliveryParam deliveryParam = new OmsOrderDeliveryParam();
        deliveryParam.setOrderId(order.getId());
        deliveryParam.setDeliveryCompany("顺丰快递");
        deliveryParam.setDeliverySn("201707196398345");
        List<OmsOrderDeliveryParam> deliveryList = Arrays.asList(deliveryParam);

        int count = orderService.delivery(deliveryList);
        // assertTrue(count > 0);

        // OmsOrder updatedOrder = orderDao.selectById(order.getId());
        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(3, updatedOrder.getStatus()); // 3 = SHIPPED
    }



    @Test
    public void testDeliveryChangesStatusFailed() {
        OmsOrder order = createTestOrder();
        OmsOrderDeliveryParam deliveryParam = new OmsOrderDeliveryParam();
        deliveryParam.setOrderId(order.getId());
        deliveryParam.setDeliveryCompany("顺丰快递");
        deliveryParam.setDeliverySn("201707196398345");
        List<OmsOrderDeliveryParam> deliveryList = Arrays.asList(deliveryParam);

        int count = orderService.delivery(deliveryList);
        assertTrue(count == 0);

        // OmsOrder updatedOrder = orderDao.selectById(order.getId());
        // // OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        // // assertEquals(3, updatedOrder.getStatus());
    }

    // 
    @Test
    public void testCloseOrdersSuccessful() {
        OmsOrder order = createTestOrder();
        order.setStatus(0);
        orderMapper.updateByPrimaryKeySelective(order);

        List<Long> ids = Arrays.asList(order.getId());
        String note = "close order ccessfully";

        int count = orderService.close(ids, note);
        assertEquals(1, count, "close successful");

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(4, updatedOrder.getStatus(), "close order status should be 4");
    }


    @Test
    public void testCloseOrdersFailure() {
        OmsOrder order = createTestOrder();
        order.setDeleteStatus(1);  
        orderMapper.updateByPrimaryKeySelective(order);

        List<Long> ids = Arrays.asList(order.getId());
        String note = "close order unsuccessfully";

        int count = orderService.close(ids, note);
        assertEquals(0, count);
    }

    // delete orders.
    @Test
    public void testDeleteOrdersSuccessful() {
        OmsOrder order = createTestOrder();
        order.setDeleteStatus(0);
        orderMapper.updateByPrimaryKeySelective(order);

        List<Long> ids = Arrays.asList(order.getId());

        int count = orderService.delete(ids);
        assertEquals(1, count, "delete successfully");

        OmsOrder deletedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(1, deletedOrder.getDeleteStatus(), "After deletion, the r deletion, tshould bedeleteStatus should be 1");
    }

    @Test
    public void testDeleteOrdersFailure() {
        OmsOrder order = createTestOrder();
        order.setDeleteStatus(1);
        orderMapper.updateByPrimaryKeySelective(order);
        List<Long> ids = Arrays.asList(order.getId());
        int count = orderService.delete(ids);
        assertEquals(0, count);
    }

    @Test
    public void tesstCloseShippedSuccess() {
        OmsOrder order = createTestOrder();
        order.setStatus(3); // Shipped
        order.setDeleteStatus(0);
        orderMapper.updateByPrimaryKeySelective(order);

        int count = orderService.close(Arrays.asList(order.getId()), "close the order");
        assertEquals(1, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(4, updatedOrder.getStatus()); // Closed
    }

    @Test
    public void TestCloseShippedFailed() {
        OmsOrder order = createTestOrder();
        order.setStatus(3); // Shipped
        order.setDeleteStatus(1); //
        orderMapper.updateByPrimaryKeySelective(order);

        int count = orderService.close(Arrays.asList(order.getId()), "try to close the shipped order");
        assertEquals(0, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(3, updatedOrder.getStatus()); // Shipped
    }

    @Test
    public void TestDeleteShippedSuccess() {
        OmsOrder order = createTestOrder();
        order.setStatus(3); // Shipped
        order.setDeleteStatus(0);
        orderMapper.updateByPrimaryKeySelective(order);

        int count = orderService.delete(Arrays.asList(order.getId()));
        assertEquals(1, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(1, updatedOrder.getDeleteStatus()); // Deleted
    }

    @Test
    public void TestDeleteShippedFailed() {
        OmsOrder order = createTestOrder();
        order.setStatus(3); // Shipped
        order.setDeleteStatus(1);    orderMapper.updateByPrimaryKeySelective(order);

        int count = orderService.delete(Arrays.asList(order.getId()));
        assertEquals(0, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(1, updatedOrder.getDeleteStatus());
        assertEquals(3, updatedOrder.getStatus());
    }

    @Test
    public void TestDeleteClosedSuccess() {
        OmsOrder order = createTestOrder();
        order.setStatus(4); // Closed
        order.setDeleteStatus(0);
        orderMapper.updateByPrimaryKeySelective(order);
        int count = orderService.delete(Arrays.asList(order.getId()));
        assertEquals(1, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(1, updatedOrder.getDeleteStatus()); // Deleted
        assertEquals(4, updatedOrder.getStatus()); // Closed
    }

    @Test
    public void TestDeleteClosedFailed() {
        OmsOrder order = createTestOrder();
        order.setStatus(4); // Closed
        order.setDeleteStatus(1); // deleted
        orderMapper.updateByPrimaryKeySelective(order);

        int count = orderService.delete(Arrays.asList(order.getId()));
        assertEquals(0, count);

        OmsOrder updatedOrder = orderMapper.selectByPrimaryKey(order.getId());
        assertEquals(1, updatedOrder.getDeleteStatus()); // deleted
        assertEquals(4, updatedOrder.getStatus()); // Closed
    }


    
}
