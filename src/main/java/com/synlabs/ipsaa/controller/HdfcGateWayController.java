package com.synlabs.ipsaa.controller;

import com.synlabs.ipsaa.service.HdfcApiDetailService;
import com.synlabs.ipsaa.view.hdfcGateWayDetails.HdfcApiDetailRequest;
import com.synlabs.ipsaa.view.hdfcGateWayDetails.HdfcApiDetailResponce;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/hdfc/")
public class HdfcGateWayController {

    @Autowired
    HdfcApiDetailService hdfcApiDetailService;

    @GetMapping("getList")
    public List<String> getDetailList(){
        return null;
    }
    @GetMapping("upload")
    public void uploadList(){
        hdfcApiDetailService.upload();
    }
    @PostMapping
    public HdfcApiDetailResponce add(@RequestBody HdfcApiDetailRequest request){
        return hdfcApiDetailService.save(request);
    }
}
