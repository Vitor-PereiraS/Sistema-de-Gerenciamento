package model.dao.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidades.Department;
import entidades.Seller;
import model.dao.SellerDao;

public class SellerDaoImpl implements SellerDao {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
	EntityManager em = emf.createEntityManager();

	@Override
	public void insert(Seller seller) {
		try {
		em.getTransaction().begin();
		em.persist(seller);
		em.getTransaction().commit();
		
		System.out.println("Vendedor incluido com sucesso, id: " + seller.getId());
		}catch(Exception e) {
			em.getTransaction().rollback();
			throw e;
	}
	}

	@Override
	public void update(Seller seller) {
		em.getTransaction().begin();
		em.getTransaction().commit();

		System.out.println("Vendedor atualizado com sucesso! ");

	}

	@Override
	public void deleteById(Integer id) {
		em.getTransaction().begin();
		Seller vendedor = em.find(Seller.class, id);
		em.remove(vendedor);
		em.getTransaction().commit();

		System.out.println("Vendedor deletado com sucesso! ");

	}

	@Override
	public Seller findById(Integer id) {

		return em.find(Seller.class, id);
	}

	@Override
	public List<Seller> findAll() {

		return em.createQuery("From Seller", Seller.class).getResultList();
	}

	@Override
	public List<Seller> findByDepartment(Department department) {
		List<Seller> lista = em
				.createQuery("Select s From Seller s " + "where s.department.id = :departmentId", Seller.class)
				.setParameter("departmentId", department.getId()).getResultList();
		return lista;
	}

}
