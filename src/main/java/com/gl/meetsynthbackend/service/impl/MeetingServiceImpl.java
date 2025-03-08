package com.gl.meetsynthbackend.service.impl;

import com.gl.meetsynthbackend.exception.BusinessException;
import com.gl.meetsynthbackend.mapper.MeetingMapper;
import com.gl.meetsynthbackend.mapper.UserMapper;
import com.gl.meetsynthbackend.pojo.Meeting;
import com.gl.meetsynthbackend.service.MeetingService;
import com.gl.meetsynthbackend.utils.CurrentHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

@Service
public class MeetingServiceImpl implements MeetingService {

    @Autowired
    private MeetingMapper meetingMapper;

    @Autowired
    private UserMapper userMapper;

//    创建会议
    @Override
    public String createMeeting(Meeting meeting) {
        String meetingId = generateUUIDMeetingId();
        meeting.setId(meetingId);
        meeting.setTime(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        if (meeting.getTitle() == null || meeting.getTitle().isEmpty()) {
            meeting.setTitle("新会议");
        }
//        String userId = CurrentHolder.getCurrentId();
//        if (userId == null) {
//            throw new BusinessException("用户不存在");
//        }
//        meeting.setUserId(userId);
//        String username = userMapper.selectUsernameByUerId(userId);
//        if (username == null) {
//            throw new BusinessException("用户不存在");
//        }
//        meeting.setUsername(username);

        try {
            int result = meetingMapper.insertMeeting(meeting);
            return result > 0 ? meetingId : null;
        } catch (Exception e) {
            // 处理数据库异常，建议记录日志
            return null;
        }
    }

    private String generateUUIDMeetingId() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 10);
    }

}
