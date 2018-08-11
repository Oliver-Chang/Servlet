package com.oliverch.resignation;

import com.oliverch.common.dao.InterfaceDAO;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;
import com.oliverch.common.result.Page;
import com.oliverch.staff.Staff;
import com.oliverch.staff.StaffDAO;
import org.apache.commons.beanutils.BeanUtils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class ResignationService {

    private InterfaceDAO<Resignation> resignationDAO;
    private InterfaceDAO<Staff> staffInterfaceDAO;

    public ResignationService() {
        resignationDAO = new ResignationDAO();
        staffInterfaceDAO = new StaffDAO();
    }

    public Result getAll() {

        return Result.success(this.resignationDAO.getALL());
    }

    public Result getById(Integer id) {
        Resignation resignation = null;
        resignation = this.resignationDAO.getById(id);
        if (resignation == null) {
            return Result.failure(ResultCode.RESULE_DATA_NONE);
        }
        return Result.success(resignation);
    }

    public Result getBy(Map<String, String[]> condition) {
        HashMap<String, String[]> hashMapCondition = new HashMap<>(condition);

        if (condition == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        List<Resignation> resignations = null;
        resignations = this.resignationDAO.getBy(hashMapCondition);

        String[] pages = hashMapCondition.get("page");
        String[] page_sizes = hashMapCondition.get("page_size");

        if ((pages != null && pages.length == 1) && (page_sizes != null && page_sizes.length == 1)) {
            hashMapCondition.remove("page");
            hashMapCondition.remove("page_size");

            Integer recordsCount = this.resignationDAO.getRecordsCount(hashMapCondition);

            Page<Resignation> page = new Page(Integer.valueOf(pages[0]), Integer.valueOf(page_sizes[0]), recordsCount, resignations);
            return Result.success(page);
        }

        return Result.success(resignations);
    }

//    public Result addOne(Resignation resignation) {
//        if (resignation == null) {
//            return Result.failure(ResultCode.PARAM_IS_INVALID);
//        }
//        Integer id;
//        if ((id = this.resignationDAO.addOne(resignation)) < 0) {
//            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
//        }
//        resignation.setrId(id);
//        return Result.success(resignation);
//    }

    public Result addOne(Resignation resignation) {
        if (resignation == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        Staff staff = staffInterfaceDAO.getById(resignation.getrId());
        if (staff == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        Integer id;
        if ((id = this.resignationDAO.addOne(resignation)) < 0) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
        if(!staffInterfaceDAO.delOne(staff)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST) ;
        }
        resignation.setrId(id);
        return Result.success(resignation);
    }

    public Result delById(Integer id) {
        Resignation resignation = this.resignationDAO.getById(id);

        if (resignation == null)
            return Result.failure(ResultCode.PARAM_IS_INVALID);

        if (!resignationDAO.delOne(resignation)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(resignation);
    }


    public Result update(Resignation resignation) {
        if (resignation == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        if (!this.resignationDAO.updateOne(resignation)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(resignation);
    }


    public static void main(String args[]) {

    }
}
