package net.nuttle.servlet;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
//If there is a RequestMapping at the class level, then anything it sets
//is final for that class. You can't change it with a @RequestMapping on a method.
//For example, this assures that all mappings in this class support both GET and POST
//@RequestMapping(method={RequestMethod.GET, RequestMethod.POST})
public class LabController {

  @RequestMapping(value="/testgetpost", method={RequestMethod.GET, RequestMethod.POST})
  @ResponseBody
  public String testGetAndPost() {
    return "this method allows get and post";
  }
  
  @RequestMapping(value="/testget", method=(RequestMethod.GET))
  @ResponseBody
  public String testGet() {
    return "this method allows get";
  }
}
