package model.dao.impl;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import entidades.Documento;
import model.dao.DocumentoDao;

public class DocumentoDaoImpl implements DocumentoDao {

	EntityManagerFactory emf = Persistence.createEntityManagerFactory("exemplo-jpa");
	EntityManager em = emf.createEntityManager();

	@Override
	public void insert(Documento department) throws RuntimeException {
		try {
			em.getTransaction().begin();
			em.persist(department);
			em.getTransaction().commit();
		} catch (RuntimeException e) {
			em.getTransaction().rollback();
			throw e;
		}
	}

	@Override
	public void update(Documento department) throws RuntimeException {
		this.insert(department);
	}

	
	@Override
	public void deleteById(Integer id) {

		Documento dep = em.find(Documento.class, id);

		em.getTransaction().begin();
		em.remove(dep);
		em.getTransaction().commit();

		System.out.println("Departamento deletado com sucesso! ");
	}

	@Override
	public Documento findById(Integer id) {

		return em.find(Documento.class, id);
	}

	@Override
	public List<Documento> findAll() {
		List<Documento> lista = new ArrayList<>();

		lista = em.createQuery("From Documento", Documento.class).getResultList();

		return lista;
	}

}
