package model.dao;

import java.util.List;

import entidades.Department;
import entidades.Documento;
import entidades.Seller;


public interface DocumentoDao {

	void insert(Documento documento);
	void update(Documento documento);
	void deleteById(Integer id);
	Documento findById(Integer id);
	List<Documento> findAll();
}
