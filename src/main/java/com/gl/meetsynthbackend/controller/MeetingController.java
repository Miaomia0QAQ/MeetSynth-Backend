package com.gl.meetsynthbackend.controller;

import com.gl.meetsynthbackend.pojo.Meeting;
import com.gl.meetsynthbackend.pojo.Result;
import com.gl.meetsynthbackend.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting")
@CrossOrigin("*")
public class MeetingController {

    @Autowired
    private MeetingService meetingService;

    @GetMapping("create")
    public Result createMeeting(Meeting meeting) {
        String res = meetingService.createMeeting(meeting);
        if (res != null) {
            return Result.success(res);
        }
        return Result.error("创建会议失败");
    }

}
