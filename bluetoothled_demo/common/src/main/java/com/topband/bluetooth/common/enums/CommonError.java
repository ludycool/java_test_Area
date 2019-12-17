package com.topband.bluetooth.common.enums;

/**
 * 通用错误代码，规则： 长度：6位 以10开头
 * 
 * @author Administrator 日期： 2018年11月23日
 */
public enum CommonError implements IErrorMessage {
    STATUS_0(0, "操作成功", null),
    STATUS_200301(200301, "账号禁止使用", null),
    STATUS_200302(200302, "账号在其他地方登录，强制下线", null),
    STATUS_200303(200303, "未登录或者会话超时", null),
    STATUS_200304(200304, "操作失败", null),
    STATUS_500301(500301, "系统错误", null);

    private int code;

    private String zhText;

    private String enText;

    private CommonError(int code, String zhText, String enText) {
        this.code = code;
        this.zhText = zhText;
        this.enText = enText;
    }

    @Override
    public String getMessage() {

        return this.zhText;
    }

    @Override
    public int getStatus() {
        return this.code;
    }

    @Override
    public IErrorMessage getError(int code) {
        IErrorMessage[] errors = CommonError.values();
        for (IErrorMessage error : errors) {
            if (code == error.getStatus()) {
                return error;
            }
        }
        return null;
    }

}
