package com.oliverch.staff;

import com.oliverch.common.dao.InterfaceDAO;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;
import com.oliverch.common.result.Page;
import com.oliverch.department.Department;
import com.oliverch.department.DepartmentDAO;
import com.oliverch.post.Post;
import com.oliverch.post.PostDao;
import com.oliverch.resignation.Resignation;
import com.oliverch.resignation.ResignationService;
import org.apache.commons.beanutils.BeanUtils;


import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class StaffService {

    private InterfaceDAO<Staff> staffDAO;
    private InterfaceDAO<Post> postInterfaceDAO;
    private InterfaceDAO<Department> departmentInterfaceDAO;
    private ResignationService resignationService;

    public StaffService() {
        staffDAO = new StaffDAO();
        postInterfaceDAO = new PostDao();
        departmentInterfaceDAO = new DepartmentDAO();
        resignationService = new ResignationService();
    }

    public Result getAll() {

        return Result.success(this.staffDAO.getALL());
    }

    public Result getById(Integer id) {
        Staff staff = null;
        staff =  this.staffDAO.getById(id);
        if (staff == null) {
            return Result.failure(ResultCode.RESULE_DATA_NONE);
        }
        return Result.success(staff);
    }

    public Result getBy(Map<String, String[]> condition) {
        HashMap<String, String[]> hashMapCondition = new HashMap<>(condition);

        if (condition == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        List<Staff> staffs = null;
        staffs = this.staffDAO.getBy(hashMapCondition);

        String[] pages = hashMapCondition.get("page");
        String[] page_sizes = hashMapCondition.get("page_size");

        if ((pages != null && pages.length == 1) && (page_sizes != null && page_sizes.length == 1)) {
            hashMapCondition.remove("page");
            hashMapCondition.remove("page_size");

            Integer recordsCount = this.staffDAO.getRecordsCount(hashMapCondition);

            Page<Staff> page = new Page(Integer.valueOf(pages[0]), Integer.valueOf(page_sizes[0]), recordsCount, staffs);
            return Result.success(page);
        }

        return Result.success(staffs);
    }

    public Result addOne(Staff staff) {
        if (staff == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        Integer recordsCount;
        Map<String, String[]> hashMap;

        hashMap = new HashMap();
        String posts[] = new String[1];
        posts[0] = staff.getPost();
        hashMap.put("postName", posts);
        recordsCount = this.postInterfaceDAO.getRecordsCount(hashMap);

        if (recordsCount == 0) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        hashMap = new HashMap();
        String department[] = new String[1];
        posts[0] = staff.getDepartment();
        hashMap.put("name", posts);
        recordsCount = this.departmentInterfaceDAO.getRecordsCount(hashMap);

        if (recordsCount == 0) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        staff.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        Integer id;

        if ((id = this.staffDAO.addOne(staff)) < 0 ) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
        staff.setrId(id);
        return Result.success(staff);
    }

    public Result delById(Integer id) {
        Staff staff = this.staffDAO.getById(id);

        if (staff == null)
            return Result.failure(ResultCode.PARAM_IS_INVALID);

        if (!staffDAO.delOne(staff)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(staff);
    }

    public Result update(Staff staff) {
        if (staff == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        if (!this.staffDAO.updateOne(staff)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(staff);
    }



    public static void main(String args[]) {
//        StaffService staffService = new StaffService();
//        Result all = staffService.getAll();
//        System.out.println(all);
        StaffDAO staffDAO = new StaffDAO();
        List<Staff> all = staffDAO.getALL();
        Staff staff = (Staff) all.get(0);
        Field[] declaredFields = staff.getClass().getDeclaredFields();
//        Gson gson = new Gson();
//        String json = gson.toJson(staff);
//        staff = gson.fromJson(json, Staff.class);
        System.out.println(declaredFields.length);
        System.out.println("delete start");
        staffDAO.delOne(staff);
        System.out.println("delete end");
        System.out.println(all);
    }
}
