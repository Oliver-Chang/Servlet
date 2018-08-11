package com.oliverch.post;


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


public class PostService {

    private InterfaceDAO<Post> postDao;
    private InterfaceDAO<Staff> staffInterfaceDAO;

    public PostService() {
        postDao = new PostDao();
        staffInterfaceDAO = new StaffDAO();
    }

    public Result getAll() {

        return Result.success(this.postDao.getALL());
    }

    public Result getById(Integer id) {
        Post post = null;
        post =  this.postDao.getById(id);
        if (post == null) {
            return Result.failure(ResultCode.RESULE_DATA_NONE);
        }
        return Result.success(post);
    }

    public Result getBy(Map<String, String[]> condition) {
        HashMap<String, String[]> hashMapCondition = new HashMap<>(condition);

        if (condition == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        List<Post> posts = null;
        posts = this.postDao.getBy(hashMapCondition);
        String[] pages = hashMapCondition.get("page");
        String[] page_sizes = hashMapCondition.get("page_size");

        if ((pages != null && pages.length == 1) && (page_sizes != null && page_sizes.length == 1)) {
            hashMapCondition.remove("page");
            hashMapCondition.remove("page_size");

            Integer recordsCount = this.postDao.getRecordsCount(hashMapCondition);

            Page<Post> page = new Page(Integer.valueOf(pages[0]), Integer.valueOf(page_sizes[0]), recordsCount, posts);
            return Result.success(page);
        }

        return Result.success(posts);
    }

    public Result addOne(Post post) {
        if (post == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }
        post.setPostId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        Integer id;
        if ((id = this.postDao.addOne(post)) < 0) {
            return Result.failure(ResultCode.SYSTEM_INNER_ERROR);
        }
        post.setrId(id);
        return Result.success(post);
    }

    public Result delById(Integer id) {
        Post post = this.postDao.getById(id);

        if (post == null)
            return Result.failure(ResultCode.PARAM_IS_INVALID);

        String [] names = new String[1];
        names[0] = post.getPostName();
        Map<String,String[]> map = new HashMap<>();
        map.put("post", names);
        Integer recordsCount = this.staffInterfaceDAO.getRecordsCount(map);
        if (recordsCount > 0) {
            return Result.failure(ResultCode.DATA_IS_UNDELETE);
        }

        if (!postDao.delOne(post)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(post);
    }

    public Result update(Post post) {
        if (post == null) {
            return Result.failure(ResultCode.PARAM_IS_INVALID);
        }

        if (!this.postDao.updateOne(post)) {
            return Result.failure(ResultCode.SPECIFIED_QUESTIONED_USER_NOT_EXIST);
        }
        return Result.success(post);
    }


    public static void main(String args[]) {
//        StaffService staffService = new StaffService();
//        Result all = staffService.getAll();
//        System.out.println(all);
        PostDao postDAO = new PostDao();
        List<Post> all = postDAO.getALL();
        Post post = (Post) all.get(0);
        Field[] declaredFields = post.getClass().getDeclaredFields();
//        Gson gson = new Gson();
//        String json = gson.toJson(staff);
//        staff = gson.fromJson(json, Staff.class);
        System.out.println(declaredFields.length);
        System.out.println("delete start");
        postDAO.delOne(post);
        System.out.println("delete end");
        System.out.println(all);
    }
}
