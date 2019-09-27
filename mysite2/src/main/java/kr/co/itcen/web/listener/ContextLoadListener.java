package kr.co.itcen.web.listener;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class ContextLoadListener implements ServletContextListener {


    public void contextInitialized(ServletContextEvent servletContextEvent)  { 
         // TODO Auto-generated method stub
    	String contextConfigLocation = servletContextEvent.getServletContext().getInitParameter("contextConfigLocation");
    	System.out.println(contextConfigLocation);
    }
    
    public void contextDestroyed(ServletContextEvent sce)  { 
         // TODO Auto-generated method stub
    	
    }
	
}
