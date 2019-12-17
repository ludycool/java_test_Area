package com.topband.bluetooth.common.enums;

/**
 * 用户模块错误码，规则： 长度：6位 以20开头
 * 
 * @author Administrator 日期： 2018年11月22日
 */
public enum BusinessError implements IErrorMessage {
    
    STATUS_200001(200001, "缺少必填参数", null),
    STATUS_200002(200002, "不正确的电子邮件格式", null),
    STATUS_200003(200003, "用户已存在", null),
    STATUS_200004(200004, "用户不存在", null),
    STATUS_200005(200005, "账号未激活", null),
    STATUS_200006(200006, "密码错误", null),
    STATUS_200007(200007, "上传文件服务器失败", null),
    STATUS_200008(200008, "参数错误", null),
    STATUS_200009(200009, "密码错误", null),
    STATUS_200010(200010, "邮件链接已使用", null),
    STATUS_200011(200011, "邮件链接已过期", null),
    STATUS_200012(200012, "两次输入密码不一致", null)
    ;

    private int code;

    private String zhText;

    private String enText;

    private BusinessError(int code, String zhText, String enText) {
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
        IErrorMessage[] errors = BusinessError.values();
        for (IErrorMessage error : errors) {
            if (code == error.getStatus()) {
                return error;
            }
        }
        return null;
    }

}
