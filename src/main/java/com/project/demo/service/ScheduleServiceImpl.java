package com.project.demo.service;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.project.demo.model.Schedule;
import com.project.demo.repository.ScheduleRepository;

@Service
public class ScheduleServiceImpl implements ScheduleService {

	@Autowired
	private ScheduleRepository schedulerepository;

	@Override
	public List<Schedule> getAllSchedules() {
		// TODO Auto-generated method stub
		return schedulerepository.findAll();
	}

	@Override
	public void saveSchedule(Schedule schedule) {
		this.schedulerepository.save(schedule);

	}

	@Override
	public Schedule getScheduleById(int id) {
		Optional<Schedule> optional = schedulerepository.findById(id);
		Schedule schedule = null;
		if (optional.isPresent()) {
			schedule = optional.get();
		} else {
			throw new RuntimeException("Schedule Not Found for id - " + id);
		}
		return schedule;
	}

	@Override
	public void deleteSchedulebyID(int id) {

		this.schedulerepository.deleteById(id);

	}

	@Override
	public List<Schedule> getAllSchedules(int id, Date date1) {

		return schedulerepository.findAll2(id, date1);
	}

//	@Override
//	public Schedule getSchedulesByDocId(int docid) {
//		
//		Optional<Schedule> optional = schedulerepository.findByDocId(docid);
//		Schedule schedule = null;
//		if (optional.isPresent()) {
//			schedule = optional.get();
//		} else {
//			schedule = new Schedule();
//		}
//		return schedule;
//	}

	@Override
	public List<Schedule> getActiveSchedules(Date date1) {
		// TODO Auto-generated method stub
		return schedulerepository.findActiveSchedules(date1);
	}

	@Override
	public List<Schedule> getInactiveSchedules(Date date1) {
		// TODO Auto-generated method stub
		return schedulerepository.findInactiveSchedules(date1);
	}

	@Override
	public List<Schedule> getActiveSchedulesByDoctor(int docid, Date date1) {

		return schedulerepository.findActiveSchedulesByDoctor(docid, date1);
	}

	@Override
	public List<Schedule> getInactiveSchedulesByDoctor(int docid, Date date1) {

		return schedulerepository.findInactiveSchedulesByDoctor(docid, date1);
	}

	@Override
	public long getScheduleCount() {
		// TODO Auto-generated method stub
		return schedulerepository.count();
	}
	

}
