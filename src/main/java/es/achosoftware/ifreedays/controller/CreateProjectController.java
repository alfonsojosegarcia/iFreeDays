package es.achosoftware.ifreedays.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.achosoftware.ifreedays.model.Project;
import es.achosoftware.ifreedays.model.Skill;
import es.achosoftware.ifreedays.model.User;
import es.achosoftware.ifreedays.repository.ProjectsRepository;
import es.achosoftware.ifreedays.repository.UserRepository;

@Controller
public class CreateProjectController {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ProjectsRepository projectsRepository;
	
	@GetMapping("/admin/createProject")
	public ModelAndView createProject() {
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("admin/createProject");
		return modelAndView;
	}
	
	@PostMapping("/admin/submitProject")
	@ResponseBody
	public String submitProject(Principal principal, @RequestParam("name") String name) {
		User user = userRepository.findByEmail(principal.getName());
		projectsRepository.save(new Project(name, user));
		String s = "";
		List<Skill> skills = projectsRepository.findSkillsByProjectAndUserId(1, user.getId());
		return s;
	}
}