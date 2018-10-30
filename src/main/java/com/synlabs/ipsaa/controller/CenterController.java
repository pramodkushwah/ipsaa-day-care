package com.synlabs.ipsaa.controller;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.ADMIN_CENTER_LIST_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_DELETE;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_READ;
import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.CENTER_WRITE;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synlabs.ipsaa.entity.center.Center;
import com.synlabs.ipsaa.ex.ValidationException;
import com.synlabs.ipsaa.service.CenterService;
import com.synlabs.ipsaa.view.center.CenterListRequest;
import com.synlabs.ipsaa.view.center.CenterRequest;
import com.synlabs.ipsaa.view.center.CenterResponse;
import com.synlabs.ipsaa.view.center.CenterResponseV2;

@RestController
@RequestMapping("/api/center/")
public class CenterController {
	@Autowired
	private CenterService centerService;

	@GetMapping
	@Secured(CENTER_READ)
	public List<CenterResponseV2> list(@RequestParam(required = false, name = "zone") String zone,
			@RequestParam(required = false, name = "city") String city) {
		return centerService.list(new CenterListRequest(zone, city)).stream().map(CenterResponseV2::new)
				.collect(Collectors.toList());
	}

	@GetMapping("all")
	@Secured(ADMIN_CENTER_LIST_READ)
	public List<CenterResponse> allCenters() {
		return centerService.listAll().stream().map(CenterResponse::new).collect(Collectors.toList());
	}

	@Secured(CENTER_WRITE)
	@PostMapping
	public CenterResponse createCenter(@RequestBody @Validated CenterRequest request, BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result.toString());
		}
		return new CenterResponse(centerService.createCenter(request));
	}

	@Secured(CENTER_WRITE)
	@PutMapping
	public CenterResponseV2 updateCenter(@RequestBody @Validated CenterRequest request, BindingResult result) {
		if (result.hasErrors()) {
			throw new ValidationException(result.toString());
		}

		Center center = centerService.updateCenter(request);
		return new CenterResponseV2(center);
	}

	@Secured(CENTER_DELETE)
	@DeleteMapping(path = "{centerId}")
	public void deleteCenter(@PathVariable Long centerId) {
		centerService.deleteCenter(new CenterRequest(centerId));
	}
}

