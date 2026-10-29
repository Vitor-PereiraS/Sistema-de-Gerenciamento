package service;

import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.RequestScoped;

import entidades.Documento;
import entidades.dto.DocumentoDto;
import exception.DbIntegrityException;
import model.dao.DocumentoDao;
import model.dao.SellerDao;
import model.dao.impl.DocumentoDaoImpl;
import model.dao.impl.SellerDaoImpl;

@RequestScoped
public class DocumentoService {

	DocumentoDao departmentDao = new DocumentoDaoImpl();
	SellerService sellerService = new SellerService();
	SellerDao sellerDao = new SellerDaoImpl();
	
	public void insert(DocumentoDto documentoDto) {
		
		validarDocumento(documentoDto);
		Documento documento = this.ConverterDTO(documentoDto);;	
		try {
			departmentDao.insert(documento);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	public void update(DocumentoDto documentoDto) throws Exception {
		validarDocumento(documentoDto);
		
		Documento documento  = this.ConverterDTO(documentoDto);
		if (documento == null) {
			throw new Exception("registro não encontrado");
		}
		departmentDao.update(documento);
	}


	public void deleteById(Integer id) {

		Documento dep = departmentDao.findById(id);

		if (dep == null) {
			throw new DbIntegrityException("Departamento não existe");
		}
		
		departmentDao.deleteById(id);
	}


	public DocumentoDto findById(Integer id) {
		Documento doc = departmentDao.findById(id);
		DocumentoDto dto = this.ConverterEntidade(doc);
		if (doc == null) {
			throw new DbIntegrityException("Departamento não existe");
		}
		
		
		return dto;
	}
	
	public List<DocumentoDto> findAll() {
		
		List<Documento> lista = departmentDao.findAll(); 
		
		if(lista.isEmpty() || lista == null){
			throw new DbIntegrityException("Departamento nãoo existe");
		}
		
		List<DocumentoDto> listaDto = new ArrayList<>();
		
		for(Documento doc : lista) {
			listaDto.add(this.ConverterEntidade(doc));
		}
		
		return listaDto;
	}
	private void validarDocumento(DocumentoDto documento) {
		
		if (documento.getNomeArquivo() == null || documento.getNomeArquivo().trim().isEmpty()) {
			throw new DbIntegrityException("campo obrigatorio");
		}
		if (documento.getConteudo() == null) {
			throw new DbIntegrityException("campo obrigatorio");
		}
		if (documento.getVendedor() == null) {
			throw new DbIntegrityException("campo obrigatorio");
		}
		
	}
	private Documento ConverterDTO(DocumentoDto documentoDTO) {
		Documento documento = new Documento();	
		documento.setNomeArquivo(documentoDTO.getNomeArquivo());
		documento.setConteudo(documentoDTO.getConteudo());
		documento.setVendedor(sellerDao.findById(documentoDTO.getVendedor().getId()));
		
		return documento;
	}
	
	private DocumentoDto ConverterEntidade(Documento documento) {
		DocumentoDto documentoDto = new DocumentoDto();	
		documentoDto.setNomeArquivo(documento.getNomeArquivo());
		documentoDto.setConteudo(documento.getConteudo());
		documentoDto.setVendedor(sellerService.findById(documento.getId()));
		
		return documentoDto;
	}
	
}
