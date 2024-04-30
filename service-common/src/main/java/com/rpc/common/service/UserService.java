package com.rpc.common.service;

import com.rpc.common.model.User;

public interface UserService {

    /**
     * 获取用户
     *
     * @param user
     * @return
     */
    User getUser(User user);

    /**
     * 用于测试 mock 接口返回值
     *
     * @return
     */
    default short getNumber() {
        return 1;
    }
}
