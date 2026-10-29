package controller.beans;

import java.io.Serializable;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import entidades.dto.DocumentoDto;
import service.DocumentoService;

@Named(value = "documento")
@ViewScoped
public class DocumentoBean implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private DocumentoService documentoService;
	
	private DocumentoDto documento;
	

}
