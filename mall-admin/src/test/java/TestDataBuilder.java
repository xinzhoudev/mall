import com.macro.mall.dto.PmsProductParam;
import com.macro.mall.dto.PmsProductQueryParam;
import com.macro.mall.dto.PmsProductResult;
import com.macro.mall.model.PmsProduct;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 测试数据构建器
 * 用于创建各种测试数据对象
 */
public class TestDataBuilder {

    /**
     * 创建商品参数对象 - 完整数据
     */
    public static PmsProductParam buildProductParam() {
        PmsProductParam param = new PmsProductParam();
        
        // 基本信息 (继承自PmsProduct)
        param.setBrandId(1L);
        param.setProductCategoryId(1L);
        param.setName("测试商品");
        param.setSubTitle("这是一个测试商品的副标题");
        param.setDescription("这是一个用于测试的商品详细描述");
        param.setProductSn("TEST-SN-123456");
        param.setPrice(new BigDecimal("99.99"));
        param.setOriginalPrice(new BigDecimal("199.99"));
        param.setStock(100);
        param.setUnit("件");
        param.setWeight(new BigDecimal("0.5"));
        param.setSort(0);
        param.setPublishStatus(1);
        param.setVerifyStatus(1);
        param.setNewStatus(0);
        // // param.setRecommendStatus(0);
        param.setDeleteStatus(0);
        
        // 扩展信息 (PmsProductParam特有)
        param.setProductLadderList(new ArrayList<>());
        param.setProductFullReductionList(new ArrayList<>());
        param.setMemberPriceList(new ArrayList<>());
        param.setSkuStockList(new ArrayList<>());
        param.setProductAttributeValueList(new ArrayList<>());
        param.setSubjectProductRelationList(new ArrayList<>());
        param.setPrefrenceAreaProductRelationList(new ArrayList<>());
        
        return param;
    }

    /**
     * 创建商品参数对象 - 最小必填项
     */
    public static PmsProductParam buildMinimalProductParam() {
        PmsProductParam param = new PmsProductParam();
        // 仅设置核心必填字段
        param.setName("最小测试商品");
        param.setPrice(new BigDecimal("1.00"));
        param.setProductSn("MIN-SN-001");
        param.setPublishStatus(0);
        param.setVerifyStatus(0);
        param.setDeleteStatus(0);
        
        // 初始化扩展列表为空
        param.setProductLadderList(new ArrayList<>());
        param.setProductFullReductionList(new ArrayList<>());
        param.setMemberPriceList(new ArrayList<>());
        param.setSkuStockList(new ArrayList<>());
        param.setProductAttributeValueList(new ArrayList<>());
        param.setSubjectProductRelationList(new ArrayList<>());
        param.setPrefrenceAreaProductRelationList(new ArrayList<>());
        
        return param;
    }

    /**
     * 创建商品查询参数 - 完整条件
     */
    public static PmsProductQueryParam buildProductQueryParam() {
        PmsProductQueryParam param = new PmsProductQueryParam();
        param.setPublishStatus(1);
        param.setVerifyStatus(1);
        param.setKeyword("手机");
        param.setBrandId(1L);
        param.setProductCategoryId(1L);
        param.setProductSn("TEST-SN");
        return param;
    }

    /**
     * 创建商品查询参数 - 仅关键词
     */
    public static PmsProductQueryParam buildQueryParamWithKeyword(String keyword) {
        PmsProductQueryParam param = new PmsProductQueryParam();
        param.setKeyword(keyword);
        return param;
    }

    /**
     * 创建商品查询参数 - 仅状态
     */
    public static PmsProductQueryParam buildQueryParamWithStatus(Integer publishStatus, Integer verifyStatus) {
        PmsProductQueryParam param = new PmsProductQueryParam();
        param.setPublishStatus(publishStatus);
        param.setVerifyStatus(verifyStatus);
        return param;
    }

    /**
     * 创建空查询参数
     */
    public static PmsProductQueryParam buildEmptyQueryParam() {
        return new PmsProductQueryParam();
    }

    /**
     * 创建商品结果对象
     */
    public static PmsProductResult buildProductResult() {
        PmsProductResult result = new PmsProductResult();
        
        // 设置商品基本信息 (继承自PmsProductParam)
        result.setBrandId(1L);
        result.setProductCategoryId(1L);
        result.setName("测试商品结果");
        result.setSubTitle("测试副标题");
        result.setDescription("测试描述");
        result.setProductSn("RESULT-SN-001");
        result.setPrice(new BigDecimal("99.99"));
        result.setOriginalPrice(new BigDecimal("199.99"));
        result.setStock(100);
        result.setPublishStatus(1);
        result.setVerifyStatus(1);
        
        // 设置PmsProductResult特有字段
        result.setCateParentId(0L);
        
        // 设置关联数据列表
        result.setProductLadderList(new ArrayList<>());
        result.setProductFullReductionList(new ArrayList<>());
        result.setMemberPriceList(new ArrayList<>());
        result.setSkuStockList(new ArrayList<>());
        result.setProductAttributeValueList(new ArrayList<>());
        result.setSubjectProductRelationList(new ArrayList<>());
        result.setPrefrenceAreaProductRelationList(new ArrayList<>());
        
        return result;
    }

    /**
     * 创建商品结果对象 - 指定ID
     */
    public static PmsProductResult buildProductResultWithId(Long id) {
        PmsProductResult result = buildProductResult();
        result.setId(id);
        result.setName("测试商品" + id);
        result.setProductSn("RESULT-SN-" + id);
        return result;
    }

    /**
     * 创建商品对象
     */
    public static PmsProduct buildProduct(Long id) {
        PmsProduct product = new PmsProduct();
        product.setId(id);
        product.setName("测试商品" + id);
        product.setProductSn("SN-" + String.format("%06d", id));
        product.setBrandId(1L);
        product.setProductCategoryId(1L);
        product.setPrice(new BigDecimal("99.99"));
        product.setOriginalPrice(new BigDecimal("199.99"));
        product.setStock(100);
        product.setUnit("件");
        product.setWeight(new BigDecimal("0.5"));
        product.setSort(id.intValue());
        product.setPublishStatus(1);
        product.setVerifyStatus(1);
        product.setNewStatus(0);
        // // product.setRecommendStatus(0);
        product.setDeleteStatus(0);
        return product;
    }

    /**
     * 创建商品列表
     */
    public static List<PmsProduct> buildProductList(int count) {
        List<PmsProduct> list = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            list.add(buildProduct((long) i));
        }
        return list;
    }

    /**
     * 创建ID列表
     */
    public static List<Long> buildIdList(int count) {
        List<Long> ids = new ArrayList<>();
        for (int i = 1; i <= count; i++) {
            ids.add((long) i);
        }
        return ids;
    }

    /**
     * 创建带特定状态的商品
     */
    public static PmsProduct buildProductWithStatus(Long id, Integer publishStatus, 
                                                     Integer verifyStatus, Integer deleteStatus) {
        PmsProduct product = buildProduct(id);
        product.setPublishStatus(publishStatus);
        product.setVerifyStatus(verifyStatus);
        product.setDeleteStatus(deleteStatus);
        return product;
    }

    /**
     * 创建未发布的商品
     */
    public static PmsProduct buildUnpublishedProduct(Long id) {
        return buildProductWithStatus(id, 0, 0, 0);
    }

    /**
     * 创建已发布待审核的商品
     */
    public static PmsProduct buildPendingProduct(Long id) {
        return buildProductWithStatus(id, 1, 0, 0);
    }

    /**
     * 创建已发布已审核的商品
     */
    public static PmsProduct buildPublishedProduct(Long id) {
        return buildProductWithStatus(id, 1, 1, 0);
    }

    /**
     * 创建已删除的商品
     */
    public static PmsProduct buildDeletedProduct(Long id) {
        return buildProductWithStatus(id, 0, 0, 1);
    }
}
