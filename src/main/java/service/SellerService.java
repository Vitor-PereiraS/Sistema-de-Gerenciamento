package service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import entidades.Department;
import entidades.Documento;
import entidades.Seller;
import entidades.dto.DepartmentDto;
import entidades.dto.DocumentoDto;
import entidades.dto.SellerDto;
import exception.DbIntegrityException;
import model.dao.DepartmentDao;
import model.dao.DocumentoDao;
import model.dao.SellerDao;
import model.dao.impl.DepartmentDaoImpl;
import model.dao.impl.DocumentoDaoImpl;
import model.dao.impl.SellerDaoImpl;

@RequestScoped
public class SellerService {

	SellerDao sellerDaoJDBC = new SellerDaoImpl();
	DepartmentDao departmentDao = new DepartmentDaoImpl();	
	DocumentoDao documentoDao = new DocumentoDaoImpl();

	public void insert(SellerDto sellerDto) {

		validarVendedor(sellerDto);
		Seller seller = new Seller(sellerDto);
		Documento doc = new Documento();
		for(DocumentoDto docDto : sellerDto.getDocumentos()) {
			doc.setNomeArquivo(docDto.getNomeArquivo());
			doc.setConteudo(docDto.getConteudo());
			doc.setVendedor(seller);
			doc.setId(docDto.getId());
			
			seller.addDocumento(doc);
		}
		try {
			
			sellerDaoJDBC.insert(seller);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void update(SellerDto sellerDto) throws Exception {

		validarVendedor(sellerDto);
		
		Seller seller = sellerDaoJDBC.findById(sellerDto.getId());
		Documento doc = new Documento();
		if (seller == null) {
			throw new Exception("vendedor não encontrado");
		}
		Department department  = departmentDao.findById(sellerDto.getDepartment().getId());
		if (department == null) {
			throw new Exception("Departamento não encontrado");
		}
		seller.setName(sellerDto.getName());
		seller.setEmail(sellerDto.getEmail());
		seller.setBirthDate(sellerDto.getBirthDate());
		seller.setBaseSalary(sellerDto.getBaseSalary());
		seller.setDepartment(department);
		
		for(DocumentoDto docDto : sellerDto.getDocumentos()) {
			doc.setNomeArquivo(docDto.getNomeArquivo());
			doc.setConteudo(docDto.getConteudo());
			doc.setVendedor(seller);
			doc.setId(docDto.getId());
			
			
			seller.addDocumento(doc);
		}
		
		sellerDaoJDBC.update(seller);
	}

	public void deleteById(Integer id) {

		if (sellerDaoJDBC.findById(id) == null) {
			throw new DbIntegrityException("Vendedor não consta na basa de dados");
		}

		sellerDaoJDBC.deleteById(id);
	}

	public SellerDto findById(Integer id) { 

		if (sellerDaoJDBC.findById(id) == null) {
			throw new DbIntegrityException("Vendedor não consta na basa de dados");
		}
		
		Seller seller = sellerDaoJDBC.findById(id);
		
		return new SellerDto(seller);
	}

	public List<SellerDto> findAll() {

		List<Seller> lista = sellerDaoJDBC.findAll();
		
		if (lista.isEmpty() || lista == null) {
			throw new DbIntegrityException("Nenhum vendedor encontrado");
		}
		
		List<SellerDto> listaDto = new ArrayList<>();
		
		for(Seller seller : lista) {
			listaDto.add(new SellerDto(seller));
		}
		
		return listaDto;
	}

	public List<SellerDto> findByDepartment(DepartmentDto departmentDto) {

		if (departmentDto == null) {
			throw new DbIntegrityException("Departamento não existe");
		}
		
		Department department = new Department(departmentDto);

		if (sellerDaoJDBC.findByDepartment(department).isEmpty()
				|| sellerDaoJDBC.findByDepartment(department) == null) {
			throw new DbIntegrityException("Nenhum vendedor encontrado");
		}

		List<Seller> lista = sellerDaoJDBC.findByDepartment(department);

		List<SellerDto> listaDto = new ArrayList<>();
		
		for(Seller seller : lista) {
			listaDto.add(new SellerDto(seller));
		}
		
		return listaDto;
	}
	

	private void validarVendedor(SellerDto seller) {
		if (seller.getName() == null || seller.getName().trim().isEmpty()) {
			throw new DbIntegrityException("campo obrigatorio");
		}

		if (seller.getEmail() == null || seller.getEmail().trim().isEmpty()) {
			throw new DbIntegrityException("campo obrigatorio");
		}

		if (seller.getBirthDate() == null) {
			throw new DbIntegrityException("campo obrigatorio");
		}

		if (seller.getBaseSalary() == null) {
			throw new DbIntegrityException("campo obrigatorio");
		}
		if (seller.getDepartment() == null) {
			throw new DbIntegrityException("campo obrigatorio");
		}
	}
	
	
}
