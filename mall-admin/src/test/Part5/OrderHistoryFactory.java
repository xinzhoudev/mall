import com.macro.mall.model.OmsOrderOperateHistory;
import java.util.Date;

public interface OrderHistoryFactory {
    OmsOrderOperateHistory create(Long orderId, 
                                  Date createTime,
                                  String operateMan, 
                                  Integer status,
                                  String note);
}



