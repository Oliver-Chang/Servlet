package com.oliverch.staff;

import com.google.gson.Gson;
import com.oliverch.common.annotation.Table;

import com.oliverch.common.dao.InterfaceDAO;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;

@Table(clazz = Staff.class ,table = "stuff", primaryKey = "rId")
public class StaffDAO implements InterfaceDAO<Staff> {

    public static void main(String args[]) {

        StaffDAO staffDAO = new StaffDAO();
        List<Staff> all = staffDAO.getALL();
        Staff staff = all.get(0);
        System.out.println(staff.toString());
        for (Object staff1 : all) {
            System.out.println("id " + staff.getId());
            staffDAO.addOne((Staff) staff1);
        }
        for (Integer i = 0; i < all.size(); i++) {
            if (i < all.size()/2)
                System.out.println("id " + staff.getId());
                staffDAO.addOne(all.get(i));
        }
    }
}
