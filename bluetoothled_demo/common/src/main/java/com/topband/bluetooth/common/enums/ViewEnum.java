package com.topband.bluetooth.common.enums;


public enum ViewEnum {
    V_T_COUNTRY("v_t_country"),
    V_D_PROJECT_SPACE("v_d_project_space"),
    V_D_PROJECT_ACCOUNT("v_d_project_account");

    ViewEnum(String name) {
        this.name = name;
    }
    private String name;

    public String getName() {
        return name;
    }
}
