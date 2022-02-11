package com.project.demo.repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.project.demo.model.Schedule;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

	@Query("SELECT s FROM Schedule s WHERE s.doctorid LIKE ?1 AND s.date >= ?2 And s.availableNumber>0")
	public List<Schedule> findAll2(int id,Date date1);
	
	@Query("SELECT s FROM Schedule s WHERE s.doctorid LIKE ?1 AND s.date >= ?2")
	public List<Schedule> findAll(int id,Date date1);
	
	@Query("SELECT s FROM Schedule s WHERE s.date < ?1")
	public List<Schedule> findInactiveSchedules(Date date1);
	
	@Query("SELECT s FROM Schedule s WHERE s.date >= ?1")
	public List<Schedule> findActiveSchedules(Date date1);
	
	@Query("SELECT s FROM Schedule s WHERE s.doctorid = ?1")
	public Optional<Schedule> findByDocId(int docid);
	
	@Query("SELECT s FROM Schedule s WHERE s.doctorid LIKE ?1 AND s.date < ?2")
	public List<Schedule> findInactiveSchedulesByDoctor(int docid, Date date1);
	
	@Query("SELECT s FROM Schedule s WHERE s.doctorid LIKE ?1 AND s.date >= ?2")
	public List<Schedule> findActiveSchedulesByDoctor(int docid, Date date1);
}
