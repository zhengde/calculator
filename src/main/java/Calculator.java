import enums.OpTypeEnums;
import java.math.BigDecimal;
import java.util.Stack;
import utils.CheckUtils;

/**
 * 加减乘除计算器
 */
public class Calculator {

    /**
     * 结果值
     */
    private ThreadLocal<BigDecimal> resultThreadLocal;
    /**
     * 历史结果数，服务于undo操作
     */
    private ThreadLocal<Stack<BigDecimal>> undoHistoryStackThreadLocal;
    /**
     * 历史结果数，服务于redo操作
     */
    private ThreadLocal<Stack<BigDecimal>> redoHistoryStackThreadLocal;

    public Calculator() {
        resultThreadLocal = new ThreadLocal<BigDecimal>();
        resultThreadLocal.set(BigDecimal.ZERO);
        undoHistoryStackThreadLocal = new ThreadLocal<Stack<BigDecimal>>();
        Stack<BigDecimal> undoHistoryStack = new Stack<BigDecimal>();
        undoHistoryStackThreadLocal.set(undoHistoryStack);
        redoHistoryStackThreadLocal = new ThreadLocal<Stack<BigDecimal>>();
        Stack<BigDecimal> redoHistoryStack = new Stack<BigDecimal>();
        redoHistoryStackThreadLocal.set(redoHistoryStack);
    }

    /**
     * 获取结果值
     */
    public BigDecimal getResult() {
        return resultThreadLocal.get();
    }

    /**
     * 重置所有
     */
    public BigDecimal reset() {
        undoHistoryStackThreadLocal.get().clear();
        redoHistoryStackThreadLocal.get().clear();
        resultThreadLocal.set(BigDecimal.ZERO);
        return resultThreadLocal.get();
    }

    /**
     * 加法操作
     *
     * @param num 被加数
     */
    public void add(BigDecimal num) {
        if (CheckUtils.checkNum(OpTypeEnums.ADD.getOpType(), num)) {
            undoHistoryStackThreadLocal.get().push(getResult());
            resultThreadLocal.set(getResult().add(num));
            redoHistoryStackThreadLocal.get().clear();
        }
    }

    /**
     * 减法操作
     *
     * @param num 被减数
     */
    public void subtract(BigDecimal num) {
        if (CheckUtils.checkNum(OpTypeEnums.SUBTRACT.getOpType(), num)) {
            BigDecimal result = getResult();
            undoHistoryStackThreadLocal.get().push(result);
            resultThreadLocal.set(result.subtract(num));
            redoHistoryStackThreadLocal.get().clear();
        }
    }

    /**
     * 乘法操作
     *
     * @param num 被乘数
     */
    public void multiply(BigDecimal num) {
        if (CheckUtils.checkNum(OpTypeEnums.MULTIPLY.getOpType(), num)) {
            BigDecimal result = getResult();
            undoHistoryStackThreadLocal.get().push(result);
            resultThreadLocal.set(result.multiply(num));
            redoHistoryStackThreadLocal.get().clear();
        }
    }

    /**
     * 除法操作，默认四舍五入，保留 2 位小数
     *
     * @param num 被除数
     */
    public void divide(BigDecimal num) {
        divide(num, 2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 除法操作
     *
     * @param num 被除数
     * @param scale 需要保留几位小数，默认保留 2 位小数
     * @param roundingMode 舍入模式，默认 BigDecimal.ROUND_HALF_UP，即四舍五入，@see BigDecimal.ROUND_XXX
     */
    public void divide(BigDecimal num, Integer scale, Integer roundingMode) {
        if (CheckUtils.checkNum(OpTypeEnums.DIVIDE.getOpType(), num)) {
            BigDecimal result = getResult();
            undoHistoryStackThreadLocal.get().push(result);
            resultThreadLocal.set(result.divide(num, scale == null ? 2 : scale,
                    roundingMode == null ? BigDecimal.ROUND_HALF_UP : roundingMode));
            redoHistoryStackThreadLocal.get().clear();
        }
    }

    /**
     * 回退到上一结果值
     */
    public void undo() {
        if (!undoHistoryStackThreadLocal.get().isEmpty()) {
            redoHistoryStackThreadLocal.get().push(getResult());
            resultThreadLocal.set(undoHistoryStackThreadLocal.get().pop());
        }
    }

    /**
     * 撤回 undo 的操作
     */
    public void redo() {
        if (!redoHistoryStackThreadLocal.get().isEmpty()) {
            undoHistoryStackThreadLocal.get().push(getResult());
            resultThreadLocal.set(redoHistoryStackThreadLocal.get().pop());
        }
    }

    public static void main(String[] args) {
        Calculator calculator = new Calculator();
        calculator.add(new BigDecimal(5));
        calculator.add(new BigDecimal(1));
        System.out.println("加后结果：" + calculator.getResult());

        calculator.subtract(new BigDecimal(1));
        System.out.println("减1后结果：" + calculator.getResult());

        calculator.multiply(new BigDecimal(2));
        System.out.println("乘2后结果：" + calculator.getResult());

        calculator.divide(new BigDecimal(2), 0, null);
        System.out.println("除2后结果：" + calculator.getResult());

        calculator.undo();
        System.out.println("undo结果：" + calculator.getResult());

        calculator.redo();
        System.out.println("redo结果：" + calculator.getResult());

        calculator.reset();
        System.out.println("reset结果：" + calculator.getResult());
    }
}
