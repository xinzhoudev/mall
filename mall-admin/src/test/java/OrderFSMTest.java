import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.macro.mall.common.api.CommonResult;
import com.macro.mall.controller.OmsOrderController;
import com.macro.mall.dto.OmsOrderDeliveryParam;
import com.macro.mall.service.OmsOrderService;

public class OrderFSMTest {

    private OmsOrderController orderController;
    private OmsOrderService orderService;

    @BeforeEach
    public void setUp() throws Exception{
        orderService = mock(OmsOrderService.class);
        orderController = new OmsOrderController();
        Field field = OmsOrderController.class.getDeclaredField("orderService");
        field.setAccessible(true);
        field.set(orderController, orderService);
    }

    @Test
    public void testDelivery_success(){
        List<OmsOrderDeliveryParam> deliveryList = Collections.singletonList(new OmsOrderDeliveryParam());
        when(orderService.delivery(deliveryList)).thenReturn(1);
        CommonResult result = orderController.delivery(deliveryList);
        assertEquals(200, result.getCode());
        // check whether it is called one time.
        verify(orderService, times(1)).delivery(deliveryList);
    }

    @Test
    public void testDelivery_failed() {
        List<OmsOrderDeliveryParam> deliveryList = Collections.singletonList(new OmsOrderDeliveryParam());
        when(orderService.delivery(deliveryList)).thenReturn(0); // Mock the delivery faliure.

        CommonResult result = orderController.delivery(deliveryList);
        assertEquals(500, result.getCode()); // CommonResult.failed, returned 500
        verify(orderService, times(1)).delivery(deliveryList);
    }

    @Test
    public void testClose_success() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(orderService.close(ids, "Close note")).thenReturn(2);

        CommonResult result = orderController.close(ids, "Close note");
        assertEquals(200, result.getCode());
        verify(orderService, times(1)).close(ids, "Close note");
    }

    @Test
    public void testClose_failed() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(orderService.close(ids, "Close note")).thenReturn(0);

        CommonResult result = orderController.close(ids, "Close note");
        assertEquals(500, result.getCode());
        verify(orderService, times(1)).close(ids, "Close note");
    }

    @Test
    public void testDelete_success() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(orderService.delete(ids)).thenReturn(2);

        CommonResult result = orderController.delete(ids);
        assertEquals(200, result.getCode());
        verify(orderService, times(1)).delete(ids);
    }

    @Test
    public void testDelete_failed() {
        List<Long> ids = Arrays.asList(1L, 2L);
        when(orderService.delete(ids)).thenReturn(0);

        CommonResult result = orderController.delete(ids);
        assertEquals(500, result.getCode());
        verify(orderService, times(1)).delete(ids);
    }


}
