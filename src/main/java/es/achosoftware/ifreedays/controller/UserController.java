package es.achosoftware.ifreedays.controller;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import es.achosoftware.ifreedays.model.Skill;
import es.achosoftware.ifreedays.model.User;
import es.achosoftware.ifreedays.service.SkillService;
import es.achosoftware.ifreedays.service.UserService;

@Controller
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private SkillService skillService;

	@RequestMapping(value = "/admin/employees", method = RequestMethod.GET)
	public ModelAndView listEmployees() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("skills", new ArrayList());
		modelAndView.setViewName("employees/list");
		modelAndView.addObject("employees", userService.listUsers());
		return modelAndView;
	}

	@RequestMapping(value = "/admin/employees/delete/{id}", method = RequestMethod.GET)
	public ModelAndView delete(@PathVariable("id") Integer id) {
		userService.delete(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("employees/list");
		modelAndView.addObject("skills", new ArrayList());
		modelAndView.addObject("employees", userService.listUsers());
		modelAndView.addObject("msg", "OK");
		return modelAndView;
	}
	
	@RequestMapping(value = "/admin/employees/delete/skill/", method = RequestMethod.GET)
	public ModelAndView deleteSkill(@RequestParam("skillId") Integer skillId, @RequestParam("userId") Integer userId) {
		User user = userService.findUserById(userId);
		Set<Skill> skills = user.getSkills().stream().filter(s -> s.getId() != skillId).collect(Collectors.toSet());
		user.setSkills(skills);
		userService.saveUser(user);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("redirect:/admin/employees/skills/" + user.getId());
		modelAndView.addObject("skills", skills);
		modelAndView.addObject("employees", userService.listUsers());
		modelAndView.addObject("msg", "OK");
		return modelAndView;
	}

	@GetMapping("/admin/employees/skills/{id}")
	public ModelAndView viewSkills(@PathVariable("id") Integer id, @RequestParam(name="msg", required=false, defaultValue="") String msg) {
//		List<String> skills = userService.findUserById(id).getSkills().stream().map(e -> e.getName())
//				.collect(Collectors.toList());
		TreeSet<Skill> skills = new TreeSet<Skill>(userService.findUserById(id).getSkills());
		User user = userService.findUserById(id);
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("employees", userService.listUsers());
		modelAndView.setViewName("employees/list");
		if (skills.size() == 0) {
			skills = null;
		}
		if (! msg.equals(""))
			modelAndView.addObject("msg", "OK");
		modelAndView.addObject("skills", skills);
		modelAndView.addObject("user", user);
		return modelAndView;
	}
	
	
}