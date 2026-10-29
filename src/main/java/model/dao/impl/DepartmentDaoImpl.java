package model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidades.Department;
import model.dao.DepartmentDao;

public class DepartmentDaoImpl implements DepartmentDao{

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
	EntityManager em = emf.createEntityManager();
	
	@Override
	public void insert(Department department) throws Exception {
		try {
		em.getTransaction().begin();
		em.persist(department);
		em.getTransaction().commit();
		}
		catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Department department)  throws Exception{
		this.insert(department);
	}
	/**
	 * anotação sobre a forma que o update é executado quando apenas abro e encerro a transação.
	 * 
	 * bla bla
	 */
	@Override
	public void updateBySession(Department department){
		em.getTransaction().begin();
		em.getTransaction().commit();
	}

	@Override
	public void deleteById(Integer id) {
		
		Department dep = em.find(Department.class, id);
		
		em.getTransaction().begin();
		em.remove(dep);
		em.getTransaction().commit();
		
		System.out.println("Departamento deletado com sucesso! ");
	}

	@Override
	public Department findById(Integer id) {
		
		return em.find(Department.class, id);
	}

	@Override
	public List<Department> findAll() {
		List<Department> lista = new ArrayList<>();
		
		lista = em.createQuery("From Department", Department.class).getResultList();
		
		return lista;
	}

}
