package com.appspot.fontheightdb;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@SuppressWarnings("serial")
public class FontNamesServlet extends HttpServlet {
  public void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws IOException {

    List<String> names = getAllFontNames();
    
    String format = req.getParameter("format");
    if (format == null || "html".equals(format)) {
      writeHtmlResponse(resp, names);
    }
    else if ("json".equals(format)) {
      writeJsonResponse(resp, names);
    }
    else if ("text".equals(format)) {
      writeTextResponse(resp, names);
    }
  }

  private List<String> getAllFontNames() {
    List<String> names = new ArrayList<String>();
    PersistenceManager pm = PMF.get().getPersistenceManager();
    try {
      DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
      Query query = new Query("Font");
      query.setKeysOnly();
      for (Entity fontEntity : datastore.prepare(query).asIterable()) {
        names.add(fontEntity.getKey().getName());
      }
    } finally {
      pm.close();
    }
    return names;
  }

  private void writeJsonResponse(HttpServletResponse resp, List<String> names)
      throws IOException {
    JSONArray jsonNames = new JSONArray();
    for (String name : names) {
      jsonNames.add(name);
    }
    JSONObject result = new JSONObject();
    result.put("names", names);
    String resultText = result.toJSONString();

    resp.setContentType("application/json");
    resp.getWriter().println(resultText);
  }

  private void writeHtmlResponse(HttpServletResponse resp, List<String> names)
      throws IOException {
    resp.setContentType("text/html");
    PrintWriter writer = resp.getWriter();
    writer.println("<html><head><title>font names</title></head><body><ul>");
    for (String name : names) {
      writer.println("<li><a href=\"/fontHeights/" + name + "\">" + name + "</a></li>");
    }
    writer.println("</ul></body></html>");
  }

  private void writeTextResponse(HttpServletResponse resp, List<String> names)
      throws IOException {
    resp.setContentType("text/plain");
    PrintWriter writer = resp.getWriter();
    for (String name : names) {
      writer.println(name);
    }
  }
}
