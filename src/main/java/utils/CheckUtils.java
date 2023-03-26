package utils;

import enums.OpTypeEnums;
import java.math.BigDecimal;

public class CheckUtils {

    /**
     * @param opType 操作类型。@see OpTypeEnums
     * @param num 被操作数值
     * @return 是否合法
     */
    public static boolean checkNum(String opType, BigDecimal num) {
        if (opType == null || num == null) {
            return false;
        }

        // 除数不能为 0
        if (OpTypeEnums.DIVIDE.getOpType().equals(opType) && BigDecimal.ZERO.equals(num)) {
            return false;
        }
        return true;
    }
}
