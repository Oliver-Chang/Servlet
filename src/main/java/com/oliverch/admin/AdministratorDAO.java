package com.oliverch.admin;

import com.oliverch.common.annotation.Table;
import com.oliverch.common.dao.InterfaceDAO;

@Table(clazz = Administrator.class, table = "admin", primaryKey = "id")
public class AdministratorDAO implements InterfaceDAO {
}
