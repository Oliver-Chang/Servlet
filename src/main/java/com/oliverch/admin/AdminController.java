package com.oliverch.admin;

import com.oliverch.common.requestparse.RequestParse;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.HashMap;


@WebServlet("/admin/authorizations")
public class AdminController extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Result result;
        PrintWriter writer = resp.getWriter();
        InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream());
        Administrator administrator = RequestParse.parseJsonRequesTo(inputStreamReader, Administrator.class);
        AdminService adminService = new AdminService();
        result = adminService.login(administrator);
        writer.write(result.toString());
    }
}
