package com.test.mvc;

import java.io.IOException;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.google.gson.Gson;
import com.test.jpa.Order;

/**
 * Handles requests for the application home page.
 */
@Controller
public class HomeController {
	
	@PersistenceContext
	private EntityManager entityManager;
	
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Transactional
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		Date date = new Date();
		DateFormat dateFormat = DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG, locale);
		
		String formattedDate = dateFormat.format(date);
		
		model.addAttribute("serverTime", formattedDate );
		
		Order order = (Order) entityManager.find(Order.class, 1L);
		model.addAttribute("order", order.toString());
		
		return "home";
	}
	
	@Transactional
	@RequestMapping(value="/order/{id}", method=RequestMethod.GET)
	public void order(
			Model model, 
			HttpServletResponse response,
			@PathVariable("id") long id
	) throws IOException {
		Order order = (Order) entityManager.find(Order.class, id);
		if(order==null) {
			response.sendError(404, "Not found");
			return;
		}
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.getWriter().println(new Gson().toJson(order));
	}
}
