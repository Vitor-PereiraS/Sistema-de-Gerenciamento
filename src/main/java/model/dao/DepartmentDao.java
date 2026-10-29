package model.dao;

import java.util.List;

import entidades.Department;



public interface DepartmentDao {

	void insert(Department department) throws Exception;
	void update(Department department) throws Exception;
	void deleteById(Integer id);
	Department findById(Integer id);
	List<Department> findAll();
	void updateBySession(Department department);
}
