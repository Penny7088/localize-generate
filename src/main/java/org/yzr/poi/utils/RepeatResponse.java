package org.yzr.poi.utils;

import org.yzr.poi.model.RepeatEntity;

import java.util.ArrayList;

public class RepeatResponse {

    private String errorMsg;

    private ArrayList<RepeatEntity> data;

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public ArrayList<RepeatEntity> getData() {
        return data;
    }

    public void setData(ArrayList<RepeatEntity> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "RepeatResponse{" +
                "errorMsg='" + errorMsg + '\'' +
                ", data=" + data +
                '}';
    }
}
