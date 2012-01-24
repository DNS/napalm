package com.github.napalm.test.freemarker;

import javax.annotation.Resource;
import javax.sql.DataSource;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.napalm.Napalm;
import com.github.napalm.freemarker.NapalmFreeMarker;
import com.github.napalm.spring.SqlQueryEngine;

@Service
@Path("/")
public class FreemarkerTestApp {

	@Autowired
	private NapalmFreeMarker freemarker;
	@Resource(name = "db")
	private DataSource db;
	@Autowired
	private SqlQueryEngine sql;

	@GET()
	@Path("/user/{user}")
	@Produces(MediaType.TEXT_PLAIN)
	public String hi(@PathParam("user") String user) {
		return freemarker.render("index.txt", "user", user, "ip", "0.0.0.0");
	}

	@SuppressWarnings("unchecked")
	@GET()
	@Path("/db")
	@Produces(MediaType.TEXT_PLAIN)
	public String db(@PathParam("user") String user) {
		return freemarker.render("db.txt", sql.queryForList(db, "tables"), sql.queryForList(db, "columns"));
	}

	public static void main(String[] args) {
		Napalm.run("src/test/resources/config/app.yml", FreemarkerTestApp.class, NapalmFreeMarker.class);
	}
}
