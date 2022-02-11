package com.project.demo.service;

import java.sql.Date;
import java.util.List;

import com.project.demo.model.Schedule;

public interface ScheduleService {
	List<Schedule> getAllSchedules();
//	Schedule getSchedulesByDocId(int docid);
	List<Schedule> getAllSchedules(int id,Date date1);
	List<Schedule> getActiveSchedules(Date date1);
	List<Schedule> getInactiveSchedules(Date date1);
	List<Schedule> getActiveSchedulesByDoctor(int docid, Date date1);
	List<Schedule> getInactiveSchedulesByDoctor(int docid, Date date1);
	void saveSchedule(Schedule schedule);
	Schedule getScheduleById(int id);
	void deleteSchedulebyID(int id);
	
	long getScheduleCount();
}
