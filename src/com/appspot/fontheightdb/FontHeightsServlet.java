package com.appspot.fontheightdb;

import com.appspot.fontheightdb.models.Font;

import java.io.IOException;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FontHeightsServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    String jsonString = null;
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      String pathInfo = req.getPathInfo();
      if (pathInfo != null) {
        String name = pathInfo.substring(1);
        Font font = pm.getObjectById(Font.class, name);
        pm.retrieve(font);
        jsonString = font.toJsonString();
      }
    } catch (JDOObjectNotFoundException ex) {
      resp.setStatus(404);
    } finally {
      pm.close();
    }

    if (jsonString != null) {
      resp.setContentType("application/json");
      resp.getWriter().println(jsonString);
    }
  }

//  public void doPost(HttpServletRequest req, HttpServletResponse resp)
//      throws IOException {
//    String jsonString = req.getParameter("data");
//    Font font = Font.fromJsonString(jsonString);
//
//    PersistenceManager pm = PMF.get().getPersistenceManager();
//    try {
//      pm.makePersistent(font);
//    } finally {
//      pm.close();
//    }
//  }
}
