import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

/**
 * 测试工具类
 * 提供JSON转换等通用功能
 */
public class TestUtils {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    static {
        // 注册Java 8时间模块
        objectMapper.registerModule(new JavaTimeModule());
    }

    /**
     * 将对象转换为JSON字符串
     */
    public static String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException("JSON序列化失败", e);
        }
    }

    /**
     * 将JSON字符串转换为对象
     */
    public static <T> T fromJson(String json, Class<T> clazz) {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException("JSON反序列化失败", e);
        }
    }
}
