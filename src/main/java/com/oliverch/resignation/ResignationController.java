package com.oliverch.resignation;


import com.oliverch.common.requestparse.RequestParse;
import com.oliverch.common.result.Result;
import com.oliverch.common.result.ResultCode;
import com.oliverch.common.routeparse.RouteParse;
import com.oliverch.staff.Staff;

import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

@WebServlet(urlPatterns = {"/resignation", "/resignation/*"})
public class ResignationController extends HttpServlet {

    private ResignationService resignationService;

    @Override
    public void init() throws ServletException {
        resignationService = new ResignationService();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Result result;
        PrintWriter writer = resp.getWriter();

        Map<String, String[]> parameterMap = req.getParameterMap();
        Iterator<Map.Entry<String, String[]>> iterator = parameterMap.entrySet().iterator();

        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {

            if (iterator.hasNext()) {
                result = this.resignationService.getBy(parameterMap);
                writer.write(result.toString());
                return;
            } else {
                result = this.resignationService.getAll();
                writer.write(result.toString());
                return;
            }
        } else {
            String param = RouteParse.parse(pathInfo, "([1-9].*)");
            if (param == null || iterator.hasNext()) {
                result = Result.failure(ResultCode.URL_PARAM_ID_IS_ERROR);
                writer.write(result.toString());
                return;
            } else {
                Integer id = Integer.valueOf(param);
                result = this.resignationService.getById(id);
                writer.write(result.toString());
                return;
            }
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Result result;
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo != null) {
            writer.write(Result.failure(ResultCode.URL_PARAM_ID_UNEXPECT).toString());
            return;
        }
        InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream());
        Resignation resignation = RequestParse.parseJsonRequesTo(inputStreamReader, Resignation.class);
        if (resignation == null) {
            writer.write(Result.failure(ResultCode.PARAM_IS_BLANK).toString());
            return;
        }

        result = this.resignationService.addOne(resignation);
        writer.write(result.toString());
        return;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Result result;
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();

        if (pathInfo == null) {
            writer.write(Result.failure(ResultCode.URL_PARAM_ID_IS_BLANK).toString());
            return;
        }


        String param = RouteParse.parse(pathInfo, "([0-9].*)");

        if (param == null) {
            writer.write(Result.failure(ResultCode.URL_PARAM_ID_IS_ERROR).toString());
            return;
        }
        Integer id = Integer.valueOf(param);
        InputStreamReader inputStreamReader = new InputStreamReader(req.getInputStream());
        Resignation resignation = RequestParse.parseJsonRequesTo(inputStreamReader, Resignation.class);
        if (resignation == null) {
            writer.write(Result.failure(ResultCode.PARAM_IS_BLANK).toString());
            return;
        }
        resignation.setrId(id);

        result = this.resignationService.update(resignation);
        writer.write(result.toString());
        return;
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Result result;
        PrintWriter writer = resp.getWriter();
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            writer.write(Result.failure(ResultCode.URL_PARAM_ID_IS_BLANK).toString());
            return;
        }

        String param = RouteParse.parse(pathInfo, "([0-9].*)");
        if (param == null) {
            writer.write(Result.failure(ResultCode.URL_PARAM_ID_IS_ERROR).toString());
            return;
        }

        Integer id = Integer.valueOf(param);

        writer.write(this.resignationService.delById(id).toString());
    }
}
