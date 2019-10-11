package zhu.liang.common.util;

import java.util.Arrays;

public class ValidateResult {

    private boolean result = false; //校验结果
    private String[] errMsg; //错误信息

    public ValidateResult(boolean result, String[] errMsg) {
        this.result = result;
        this.errMsg = errMsg;
    }

    public boolean isSuccess() {
        return result;
    }

    public String[] getErrMsg() {
        return errMsg;
    }

    /**
     * @see Object#toString()
     */
    @Override
    public String toString() {
        return "ValidateResult [result=" + result +
                ", errMsg=" + Arrays.toString(errMsg) + "]";
    }

}
