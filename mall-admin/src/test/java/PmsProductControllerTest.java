import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.macro.mall.controller.PmsProductController;
import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;
import com.macro.mall.service.PmsProductService;

/**
 * 使用 JUnit 5 + Mockito + MockMvc
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("Controller test")
class PmsProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private PmsProductService productService;

    @InjectMocks
    private PmsProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    @DisplayName("Get product info: success")
    void testGetUpdateInfo_Success() throws Exception {
        // Given
        Long productId = 1L;
        PmsProductResult productResult = createMockProductResult();
        when(productService.getUpdateInfo(productId)).thenReturn(productResult);

        // When & Then
        mockMvc.perform(get("/product/updateInfo/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").exists());

        verify(productService, times(1)).getUpdateInfo(productId);
    }

    @Test
    @DisplayName("Get product into-ID not exist")
    void testGetUpdateInfo_NotFound() throws Exception {
        // Given
        Long productId = 999L;
        when(productService.getUpdateInfo(productId)).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/product/updateInfo/{id}", productId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").isEmpty());

        verify(productService, times(1)).getUpdateInfo(productId);
    }

    @Test
    @DisplayName("Inquery product result-Empty")
    void testGetList_EmptyResult() throws Exception {
        // Given
        when(productService.list(any(PmsProductQueryParam.class), anyInt(), anyInt()))
                .thenReturn(Collections.emptyList());

        // When & Then
        mockMvc.perform(get("/product/list")
                        .param("pageNum", "1")
                        .param("pageSize", "10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(productService, times(1)).list(any(PmsProductQueryParam.class), anyInt(), anyInt());
    }

    @Test
    @DisplayName("check status-failure")
    void testUpdateVerifyStatus_Failed() throws Exception {
        // Given
        when(productService.updateVerifyStatus(anyList(), anyInt(), anyString())).thenReturn(0);

        // When & Then
        mockMvc.perform(post("/product/update/verifyStatus")
                        .param("ids", "1")
                        .param("verifyStatus", "1")
                        .param("detail", "审核通过"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(500));

        verify(productService, times(1)).updateVerifyStatus(anyList(), anyInt(), anyString());
    }

    
    @Test
    @DisplayName("cancel products-success")
    void testUpdatePublishStatus_Unpublish_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);
        Integer publishStatus = 0; // 下架
        when(productService.updatePublishStatus(ids, publishStatus)).thenReturn(2);

        // When & Then
        mockMvc.perform(post("/product/update/publishStatus")
                        .param("ids", "1", "2")
                        .param("publishStatus", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(2));

        verify(productService, times(1)).updatePublishStatus(anyList(), eq(publishStatus));
    }

    
    @Test
    @DisplayName("recommendation of products-success")
    void testUpdateRecommendStatus_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);
        Integer recommendStatus = 1;
        when(productService.updateRecommendStatus(ids, recommendStatus)).thenReturn(2);

        // When & Then
        mockMvc.perform(post("/product/update/recommendStatus")
                        .param("ids", "1", "2")
                        .param("recommendStatus", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(2));

        verify(productService, times(1)).updateRecommendStatus(anyList(), eq(recommendStatus));
    }

    @Test
    @DisplayName("cancel recommendation-success")
    void testUpdateRecommendStatus_Cancel_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L);
        Integer recommendStatus = 0;
        when(productService.updateRecommendStatus(ids, recommendStatus)).thenReturn(1);

        // When & Then
        mockMvc.perform(post("/product/update/recommendStatus")
                        .param("ids", "1")
                        .param("recommendStatus", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(productService, times(1)).updateRecommendStatus(anyList(), eq(recommendStatus));
    }

    @Test
    @DisplayName("set products-success")
    void testUpdateNewStatus_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L, 3L);
        Integer newStatus = 1;
        when(productService.updateNewStatus(ids, newStatus)).thenReturn(3);

        // When & Then
        mockMvc.perform(post("/product/update/newStatus")
                        .param("ids", "1", "2", "3")
                        .param("newStatus", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(3));

        verify(productService, times(1)).updateNewStatus(anyList(), eq(newStatus));
    }

    // ==================== 批量修改删除状态测试 ====================
    
    @Test
    @DisplayName("delete products-success")
    void testUpdateDeleteStatus_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L, 2L);
        Integer deleteStatus = 1;
        when(productService.updateDeleteStatus(ids, deleteStatus)).thenReturn(2);

        // When & Then
        mockMvc.perform(post("/product/update/deleteStatus")
                        .param("ids", "1", "2")
                        .param("deleteStatus", "1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200))
                .andExpect(jsonPath("$.data").value(2));

        verify(productService, times(1)).updateDeleteStatus(anyList(), eq(deleteStatus));
    }

    @Test
    @DisplayName("restore products-success")
    void testUpdateDeleteStatus_Restore_Success() throws Exception {
        // Given
        List<Long> ids = Arrays.asList(1L);
        Integer deleteStatus = 0;
        when(productService.updateDeleteStatus(ids, deleteStatus)).thenReturn(1);

        // When & Then
        mockMvc.perform(post("/product/update/deleteStatus")
                        .param("ids", "1")
                        .param("deleteStatus", "0"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.code").value(200));

        verify(productService, times(1)).updateDeleteStatus(anyList(), eq(deleteStatus));
    }

    // ==================== appendix methods ====================
    
    private PmsProductParam createMockProductParam() {
        return TestDataBuilder.buildProductParam();
    }

    private PmsProductResult createMockProductResult() {
        return TestDataBuilder.buildProductResult();
    }

    private List<PmsProduct> createMockProductList() {
        return TestDataBuilder.buildProductList(2);
    }

    private String toJson(Object obj) {
        return TestUtils.toJson(obj);
    }
}
