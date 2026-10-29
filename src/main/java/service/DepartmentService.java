package service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import entidades.Department;
import entidades.dto.DepartmentDto;
import exception.DbIntegrityException;
import model.dao.DepartmentDao;
import model.dao.impl.DepartmentDaoImpl;

@RequestScoped
public class DepartmentService {

	DepartmentDao departmentDao = new DepartmentDaoImpl();	
	
	public void insert(DepartmentDto departmentDto) {
		
		validarDepartamento(departmentDto);
		Department department = new Department(departmentDto);	
		try {
			departmentDao.insert(department);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void update(DepartmentDto departmentDto) throws Exception {
		validarDepartamento(departmentDto);
		Department department  = departmentDao.findById(departmentDto.getId());
		if (department == null) {
			throw new Exception("registro não encontrado");
		}
		department.setName(departmentDto.getName());
		departmentDao.update(department);
	}


	public void deleteById(Integer id) {

		Department dep = departmentDao.findById(id);

		if (dep == null) {
			throw new DbIntegrityException("Departamento não existe");
		}
		
		departmentDao.deleteById(id);
	}


	public DepartmentDto findById(Integer id) {
		Department dep = departmentDao.findById(id);
		
		if (dep == null) {
			throw new DbIntegrityException("Departamento não existe");
		}
		
		
		return new DepartmentDto(dep);
	}
	
	public List<DepartmentDto> findAll() {
		
		List<Department> lista = departmentDao.findAll(); 
		
		if(lista.isEmpty() || lista == null){
			throw new DbIntegrityException("Departamento nãoo existe");
		}
		
		List<DepartmentDto> listaDto = new ArrayList<>();
		
		for(Department dep : lista) {
			listaDto.add(new DepartmentDto(dep));
		}
		
		return listaDto;
	}
	private void validarDepartamento(DepartmentDto department) {
		
		if (department.getName() == null || department.getName().trim().isEmpty()) {
			throw new DbIntegrityException("campo obrigatorio");
		}
		
	}
}
