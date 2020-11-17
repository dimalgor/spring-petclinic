/*
 * Copyright 2012-2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.samples.petclinic.position;

import org.springframework.samples.petclinic.visit.VisitRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Collection;
import java.util.Map;

@Controller
class PositionsController {

	private static final String VIEWS_POSITION_CREATE_OR_UPDATE_FORM = "positions/createOrUpdatePositionForm";

	private final PositionsRepository positions;

	private VisitRepository visits;

	public PositionsController(PositionsRepository clinicService, VisitRepository visits) {
		this.positions = clinicService;
		this.visits = visits;
	}

	@InitBinder
	public void setAllowedFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/positions/new")
	public String initCreationForm(Map<String, Object> model) {
		Position position = new Position();
		model.put("position", position);
		return VIEWS_POSITION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/positions/new")
	public String processCreationForm(@Valid Position position, BindingResult result) {
		if (result.hasErrors()) {
			return VIEWS_POSITION_CREATE_OR_UPDATE_FORM;
		}
		else {
			this.positions.save(position);
			return "redirect:/positions/" + position.getId();
		}
	}

	@GetMapping("/positions/find")
	public String initFindForm(Map<String, Object> model) {
		model.put("position", new Position());
		return "positions/findPositions";
	}

	@GetMapping("/positions")
	public String processFindForm(Position position, BindingResult result, Map<String, Object> model) {

		// allow parameterless GET request for /positions to return all records
		if (position.getPosition() == null) {
			position.setPosition(""); // empty string signifies broadest possible search
		}

		// find positions by last name
		Collection<Position> results = this.positions.findByPosition(position.getPosition());
		if (results.isEmpty()) {
			// no positions found
			result.rejectValue("area", "notFound", "not found");
			return "positions/findPositions";
		}
		else if (results.size() == 1) {
			// 1 owner found
			position = results.iterator().next();
			return "redirect:/positions/" + position.getId();
		}
		else {
			// multiple positions found
			model.put("selections", results);
			return "positions/positionsList";
		}
	}

	@GetMapping("/positions/{positionId}/edit")
	public String initUpdatePositionForm(@PathVariable("positionId") int positionId, Model model) {
		Position position = this.positions.findById(positionId);
		model.addAttribute(position);
		return VIEWS_POSITION_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/positions/{positionId}/edit")
	public String processUpdatePositionForm(@Valid Position position, BindingResult result,
											@PathVariable("positionId") int positionId) {
		if (result.hasErrors()) {
			return VIEWS_POSITION_CREATE_OR_UPDATE_FORM;
		}
		else {
			position.setId(positionId);
			this.positions.save(position);
			return "redirect:/positions/{positionId}";
		}
	}

	/**
	 * Custom handler for displaying position.
	 * @param positioId the ID of the position to display
	 * @return a ModelMap with the model attributes for the view
	 */
	@GetMapping("/positions/{positionId}")
	public ModelAndView showPosition(@PathVariable("positionId") int positioId) {
		ModelAndView mav = new ModelAndView("positions/positionDetails");
		Position position = this.positions.findById(positioId);
		for (Pet pet : position.getPets()) {
			pet.setVisitsInternal(visits.findByPetId(pet.getId()));
		}
		mav.addObject(position);
		return mav;
	}

}
