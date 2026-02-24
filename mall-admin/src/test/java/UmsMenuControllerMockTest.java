import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.macro.mall.common.api.CommonResult;

import com.macro.mall.model.UmsMenu;
import com.macro.mall.service.UmsMenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.context.ContextConfiguration;

import com.macro.mall.MallAdminApplication;
import com.macro.mall.controller.UmsMenuController;
import com.macro.mall.dto.UmsMenuNode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(value = UmsMenuController.class,
            excludeAutoConfiguration = SecurityAutoConfiguration.class)
@ContextConfiguration(classes = MallAdminApplication.class)
public class UmsMenuControllerMockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UmsMenuService menuService;

    private UmsMenu testMenu;

    @BeforeEach
    public void setup() {
        testMenu = new UmsMenu();
        testMenu.setName("测试菜单");
        testMenu.setParentId(0L);
        testMenu.setHidden(0);
        testMenu.setSort(0);
        testMenu.setIcon("icon-test");
    }

    @Test
    public void testCreateMenuMock() throws Exception {
        // 1. Mock service 返回 1（表示插入成功）
        Mockito.when(menuService.create(any(UmsMenu.class))).thenReturn(1);

        // 2. 发送 POST 请求
        MvcResult result = mockMvc.perform(post("/menu/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMenu)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response, "响应体不应为空");
        assertFalse(response.isEmpty(), "响应体不应为空字符串");

        CommonResult<Integer> commonResult = objectMapper.readValue(
                response,
                new TypeReference<CommonResult<Integer>>() {}
        );

        assertEquals(200L, commonResult.getCode(), "响应码应为200");
        assertEquals(1, commonResult.getData(), "返回数据应为1");

        Mockito.verify(menuService, Mockito.times(1)).create(any(UmsMenu.class));
    }

    @Test
    public void testCreateMenuFailed() throws Exception {
        Mockito.when(menuService.create(any(UmsMenu.class))).thenReturn(0);

        MvcResult result = mockMvc.perform(post("/menu/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMenu)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        // 插入失败，code 不应为 200
        assertNotEquals(200L, commonResult.getCode(), "Response code should not be 200 when creation fails");
        Mockito.verify(menuService, Mockito.times(1)).create(any(UmsMenu.class));
    }

    // ==================== testUpdateMenu ====================
    @Test
    public void testUpdateMenu() throws Exception {
        Mockito.when(menuService.update(eq(1L), any(UmsMenu.class))).thenReturn(1);

        testMenu.setName("menu updated");
        MvcResult result = mockMvc.perform(post("/menu/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMenu)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertEquals(200L, commonResult.getCode(), "return data rn code 为200");
        assertEquals(1, commonResult.getData(), "返回数据应为1");
        Mockito.verify(menuService, Mockito.times(1)).update(eq(1L), any(UmsMenu.class));
    }

    // ==================== testUpdateMenu 失败场景 ====================
    @Test
    public void testUpdateMenuFailed() throws Exception {
        Mockito.when(menuService.update(eq(1L), any(UmsMenu.class))).thenReturn(0);

        MvcResult result = mockMvc.perform(post("/menu/update/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testMenu)))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertNotEquals(200L, commonResult.getCode(), "return code should not be when failed");
        Mockito.verify(menuService, Mockito.times(1)).update(eq(1L), any(UmsMenu.class));
    }

    // ==================== testGetMenuItem ====================
    @Test
    public void testGetMenuItem() throws Exception {
        Mockito.when(menuService.getItem(eq(1L))).thenReturn(testMenu);

        MvcResult result = mockMvc.perform(get("/menu/1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        CommonResult<UmsMenu> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<UmsMenu>>() {});

        assertEquals(200L, commonResult.getCode(), "return data should not be emptyrn code 为200");
        assertNotNull(commonResult.getData(), "返回数据不应为空");
        assertEquals("测试菜单", commonResult.getData().getName(), "菜单名称应匹配");
        Mockito.verify(menuService, Mockito.times(1)).getItem(eq(1L));
    }

    // ==================== testDeleteMenu ====================
    @Test
    public void testDeleteMenu() throws Exception {
        Mockito.when(menuService.delete(eq(1L))).thenReturn(1);

        MvcResult result = mockMvc.perform(post("/menu/delete/1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertEquals(200L, commonResult.getCode(), "响应码应为200");
        assertEquals(1, commonResult.getData(), "返回数据应为1");
        Mockito.verify(menuService, Mockito.times(1)).delete(eq(1L));
    }

    // ==================== testDeleteMenu 失败场景 ====================
    @Test
    public void testDeleteMenuFailed() throws Exception {
        Mockito.when(menuService.delete(eq(1L))).thenReturn(0);

        MvcResult result = mockMvc.perform(post("/menu/delete/1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertNotEquals(200L, commonResult.getCode(), "删除失败时响应码不应为200");
        Mockito.verify(menuService, Mockito.times(1)).delete(eq(1L));
    }

    // ==================== testListMenus ====================
    @Test
    public void testListMenus() throws Exception {
        List<UmsMenu> menuList = Arrays.asList(testMenu);
        Mockito.when(menuService.list(eq(0L), eq(5), eq(1))).thenReturn(menuList);

        MvcResult result = mockMvc.perform(get("/menu/list/0")
                        .param("pageSize", "5")
                        .param("pageNum", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        assertNotNull(response);
        assertTrue(response.contains("\"list\""), "响应应包含list字段");
        Mockito.verify(menuService, Mockito.times(1)).list(eq(0L), eq(5), eq(1));
    }

    // ==================== testTreeList ====================
    @Test
    public void testTreeList() throws Exception {
        UmsMenuNode node = new UmsMenuNode();
        node.setName("测试菜单");
        node.setChildren(Arrays.asList());
        List<UmsMenuNode> nodeList = Arrays.asList(node);
        Mockito.when(menuService.treeList()).thenReturn(nodeList);

        MvcResult result = mockMvc.perform(get("/menu/treeList"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<List<UmsMenuNode>> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<List<UmsMenuNode>>>() {});

        assertEquals(200L, commonResult.getCode(), "响应码应为200");
        assertNotNull(commonResult.getData(), "返回数据不应为空");
        assertFalse(commonResult.getData().isEmpty(), "树形列表不应为空");
        Mockito.verify(menuService, Mockito.times(1)).treeList();
    }

    // ==================== testUpdateHidden ====================
    @Test
    public void testUpdateHidden() throws Exception {
        Mockito.when(menuService.updateHidden(eq(1L), eq(1))).thenReturn(1);

        MvcResult result = mockMvc.perform(post("/menu/updateHidden/1")
                        .param("hidden", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertEquals(200L, commonResult.getCode(), "响应码应为200");
        assertEquals(1, commonResult.getData(), "返回数据应为1");
        Mockito.verify(menuService, Mockito.times(1)).updateHidden(eq(1L), eq(1));
    }

    // ==================== testUpdateHidden failed ====================
    @Test
    public void testUpdateHiddenFailed() throws Exception {
        Mockito.when(menuService.updateHidden(eq(1L), eq(1))).thenReturn(0);

        MvcResult result = mockMvc.perform(post("/menu/updateHidden/1")
                        .param("hidden", "1"))
                .andExpect(status().isOk())
                .andReturn();

        String response = result.getResponse().getContentAsString();
        CommonResult<Integer> commonResult = objectMapper.readValue(
                response, new TypeReference<CommonResult<Integer>>() {});

        assertNotEquals(200L, commonResult.getCode(), "修改失败时响应码不应为200");
        Mockito.verify(menuService, Mockito.times(1)).updateHidden(eq(1L), eq(1));
    }
}





