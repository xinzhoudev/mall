package com.macro.mall.portal;

import com.macro.mall.portal.dao.PortalProductDao;
import com.macro.mall.portal.domain.PromotionProduct;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PortalProductDaoPartitionTest{
    @Autowired
    private PortalProductDao portalProductDao;
    @Test
    public void testEmptyList(){
        assertEquals(0, portalProductDao.getPromotionProductList(new ArrayList<>()).size());
    }

    @Test
    public void testNormalValidId(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(50L);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        assertEquals(ids.size(), result.size());
    }

    @Test
    public void testNegativeId(){
        List<Long> ids = new ArrayList<>();
        ids.add(-1L);
        ids.add(-100L);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        assertEquals(0, result.size());
    }

    @Test
    public void testZeroId(){
        List<Long> ids = new ArrayList<>();
        ids.add(0L);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        // zero is not allowd in mySQL database.
        assertEquals(0, result.size());
    }

    @Test
    public void testMaxLongId(){
        List<Long> ids = new ArrayList<>();
        ids.add(Long.MAX_VALUE);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        // the maxValue of Long should be valid.
        assertEquals(1, result.size());
    }

    @Test
    public void testPartialValid(){
        List<Long> ids = new ArrayList<>();
        ids.add(1L);
        ids.add(-1L);
        ids.add(999_999_999_999L);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        assertEquals(1, result.size());
    }

    @Test
    public void testAllInvalid(){
        List<Long> ids = new ArrayList<>();
        ids.add(-1L);
        ids.add(-2L);
        ids.add(-Long.MAX_VALUE);
        List<PromotionProduct> result = portalProductDao.getPromotionProductList(ids);
        assertEquals(0, result.size());

    }


}



