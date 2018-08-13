package com.oliverch.department;

import com.oliverch.common.dao.InterfaceDAO;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;
import com.oliverch.common.result.Page;
import com.oliverch.staff.Staff;
import com.oliverch.staff.StaffDAO;


import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class DepartmentService {

    private InterfaceDAO<Department> departmentDAO;
    private InterfaceDAO<Staff> staffInterfaceDAO;

    public DepartmentService() {
        departmentDAO = new DepartmentDAO();
        staffInterfaceDAO = new StaffDAO();
    }

    public Result getAll() {
        Result success = Result.success(this.departmentDAO.getALL());
        System.out.println("department");
        System.out.println(success);
        return success;
    }

    public Result getById(Integer id) {
        Department department = null;
        department = this.departmentDAO.getById(id);
        if (department == null) {
            return Result.failure(ResultCode.RESULE_DATA_NONE);
        }
        return Result.success(department);
    }

    public Result getBy(Map<String, String[]> condition) {
        HashMap<String, String[]> hashMapCondition = new HashMap<>(condition);

        if (condition == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        List<Department> departments = null;
        departments = this.departmentDAO.getBy(hashMapCondition);
        String[] pages = hashMapCondition.get("page");
        String[] page_sizes = hashMapCondition.get("page_size");

        if ((pages != null && pages.length == 1) && (page_sizes != null && page_sizes.length == 1)) {
            hashMapCondition.remove("page");
            hashMapCondition.remove("page_size");

            Integer recordsCount = this.departmentDAO.getRecordsCount(hashMapCondition);

            Page<Department> page = new Page(Integer.valueOf(pages[0]), Integer.valueOf(page_sizes[0]), recordsCount, departments);
            return Result.success(page);
        }

        return Result.success(departments);
    }

    public Result addOne(Department department) {
        if (department == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        department.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        Integer id;
        if ((id = this.departmentDAO.addOne(department)) < 0) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
        department.setrId(id);
        return Result.success(department);
    }

    public Result delById(Integer id) {
        Department department = this.departmentDAO.getById(id);

        if (department == null)
            return Result.failure(ResultCode.PARAM_IS_INVALID);

        // 判断能否删除
        String [] names = new String[1];
        names[0] = department.getName();
        Map<String,String[]> map = new HashMap<>();
        map.put("department", names);
        Integer recordsCount = this.staffInterfaceDAO.getRecordsCount(map);
        if (recordsCount > 0) {
            return Result.failure(ResultCode.DATA_IS_UNDELETE);
        }

        if (!departmentDAO.delOne(department)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(department);
    }

    public Result update(Department department) {
        if (department == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        if (!this.departmentDAO.updateOne(department)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(department);
    }


    public static void main(String args[]) {
//        StaffService staffService = new StaffService();
//        Result all = staffService.getAll();
//        System.out.println(all);
        DepartmentDAO staffDAO = new DepartmentDAO();
        List<Department> all = staffDAO.getALL();
        Department department = (Department) all.get(0);
        Field[] declaredFields = department.getClass().getDeclaredFields();
//        Gson gson = new Gson();
//        String json = gson.toJson(staff);
//        staff = gson.fromJson(json, Staff.class);
        System.out.println(declaredFields.length);
        System.out.println("delete start");
        staffDAO.delOne(department);
        System.out.println("delete end");
        System.out.println(all);
    }
}
