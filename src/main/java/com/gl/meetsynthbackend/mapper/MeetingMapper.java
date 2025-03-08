package com.gl.meetsynthbackend.mapper;

import com.gl.meetsynthbackend.pojo.Meeting;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface MeetingMapper {
    int insertMeeting(Meeting meeting);
}
