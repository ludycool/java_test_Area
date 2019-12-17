package com.topband.bluetooth.common.enums;

public interface IErrorMessage {
    
    public String getMessage();
    
    public int getStatus();
    
    public IErrorMessage getError(int code);

}
