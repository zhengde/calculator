package enums;

public enum OpTypeEnums {
    ADD("ADD", "加"),
    SUBTRACT("SUBTRACT", "减"),
    MULTIPLY("MULTIPLY", "乘"),
    DIVIDE("DIVIDE", "除");

    /**
     * 操作类型
     */
    String opType;
    /**
     * 描述
     */
    String desc;

    OpTypeEnums(String opType, String desc) {
        this.opType = opType;
        this.desc = desc;
    }

    public String getOpType() {
        return opType;
    }

    public String getDesc() {
        return desc;
    }
}
