package com.oliverch.admin;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.oliverch.authorization.AuthManager;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;

import java.util.HashMap;
import java.util.List;

public class AdminService {

    public Result login(Administrator admin) {
        Result result;
        String token = null;
        AdministratorDAO administratorDAO = new AdministratorDAO();
        String conditionValues[] = new String[1];
        conditionValues[0] = admin.getUsername();
        HashMap<String, String[]> condition = new HashMap<>();
        condition.put("username", conditionValues);
        List<Administrator> admins = administratorDAO.getBy(condition);
        if (admins.size() == 0) {
            return Result.failure(ResultCode.USER_LOGIN_ERROR);
        }
        if (admins.get(0).getPassword().equals(admin.getPassword())) {
            token = AuthManager.createToken(admin);
            HashMap<String, String> map = new HashMap<>();
            map.put("Token", token);
            return Result.success(map);
        } else {
            return Result.failure(ResultCode.USER_LOGIN_ERROR);
        }
    }


}
