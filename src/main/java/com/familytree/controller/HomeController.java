package com.familytree.controller;

import com.familytree.logic.FamilyTree;
import com.familytree.model.ResponseTree;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Controller
public class HomeController {
    @Resource
    FamilyTree ft;
    private static Map<Integer, String> map = new HashMap<>();

    static {
        map.put(1, "abc");
        map.put(2, "de");
        map.put(3, "tgh");
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView printWelcome(ModelMap model) {
        return new ModelAndView("index", model);

    }

    @RequestMapping(value = "/getTree", method = RequestMethod.GET)
    public
    @ResponseBody
    ResponseTree getTree() {

        return ft.getRoot();


    }
    /*@RequestMapping(value="/getData",method=RequestMethod.POST)
    public @ResponseBody String getData(@RequestBody Intm id){
		
		return map.get(Integer.parseInt(id.getId()));
	}*/
}