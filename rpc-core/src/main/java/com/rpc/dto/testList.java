package com.rpc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class testList <T>{
    private T data;
    public Class<?> getDataType(){
        return data.getClass();
    }
}
