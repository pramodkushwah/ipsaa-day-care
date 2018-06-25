package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.entity.programs.GroupActivity;
import com.synlabs.ipsaa.service.GroupActivityService;
import com.synlabs.ipsaa.view.center.GroupActivityRequest;
import com.synlabs.ipsaa.view.center.GroupActivityResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

import static com.synlabs.ipsaa.auth.IPSAAAuth.Privileges.*;
@RestController
@RequestMapping("/api/group/activity")
public class GroupActivityController
{
  @Autowired
  private GroupActivityService groupActivityService;



  @GetMapping
  @Secured(GROUP_ACTIVITY_READ)
  public List<GroupActivityResponse> list(@RequestParam(name="centerId",required = false) Long centerId,
                                          @RequestParam(name="groupId",required = false) Long groupId){
    GroupActivityRequest request=new GroupActivityRequest();
    request.setCenterId(centerId);
    request.setGroupId(groupId);
    return groupActivityService.list(request).stream().map(GroupActivityResponse::new).collect(Collectors.toList());
  }

  @PostMapping
  @Secured(GROUP_ACTIVITY_WRITE)
  public GroupActivityResponse save(@RequestBody GroupActivityRequest request){
    return new GroupActivityResponse(groupActivityService.save(request));
  }

  @PutMapping
  @Secured(GROUP_ACTIVITY_WRITE)
  public GroupActivityResponse update(@RequestBody GroupActivityRequest request){
    return new GroupActivityResponse(groupActivityService.update(request));
  }


}
