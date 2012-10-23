package controllers;

import play.*;
import play.db.jpa.JPA;
import play.mvc.*;

import java.util.*;

import javax.persistence.Query;

import models.*;

public class Application extends Controller {

    public static void index() {
    	//Test code for accessing database
    	Query query=JPA.em().createQuery("select empName from Employee");
     	
     	List<Object> listSurveyResult=query.getResultList();
     	Logger.info("number employee records fetched"+listSurveyResult.size());
        render();
    }

}