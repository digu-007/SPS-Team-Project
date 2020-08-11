package com.google.sps.servlets;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.sps.data.BaseEntity;
import com.google.gson.Gson;
import java.util.List;
import java.util.ArrayList;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.SortDirection;

/* Servlet that handles retrieving items from todo list */
@WebServlet("/todo_list")
public class TodoListServlet extends HttpServlet {
    
    private static final String TODO_ENTITY = "TodoEntity";
    private static final String TIMESTAMP_PROPERTY = "timestamp";
    private static final String ENTITY_TYPE_PROPERTY = "EntityType";
    private static final String ENTITY_ID_PROPERTY = "EntityID";
    private static final Gson GSON = new Gson();

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Query query = new Query(TODO_ENTITY).addSort(TIMESTAMP_PROPERTY, SortDirection.DESCENDING);;
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery results = datastore.prepare(query);
        List<BaseEntity> todo_list = new ArrayList<>();
        results.asIterable().forEach(entity -> {
            BaseEntity baseEntityObj = BaseEntity.builder().id((long)entity.getProperty(ENTITY_ID_PROPERTY)).type(
            (long)entity.getProperty(ENTITY_TYPE_PROPERTY)).build();
            todo_list.add(baseEntityObj);
        });
        response.setContentType("application/json");
        response.getWriter().println(GSON.toJson(todo_list));
    }
}