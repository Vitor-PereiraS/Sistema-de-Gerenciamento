package controller.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;

import entidades.Documento;
import entidades.dto.DocumentoDto;
import entidades.dto.SellerDto;
import exception.DbIntegrityException;
import service.DocumentoService;
import service.SellerService;

@Named(value = "documento")
@ViewScoped
public class DocumentoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private DocumentoService documentoService;

	@Inject
	private SellerService sellerService;

	private DocumentoDto documento;
	private DocumentoDto documentoSelecionado;

	private Integer vendedorSelecionado;

	
	private Integer idVendedorSelecionado;

	private List<DocumentoDto> listaDocumentos;
	private List<DocumentoDto> listaDocumentosFiltrados;

	private byte[] bytesDoNovoArquivo;

	private Logger logger = Logger.getLogger(getClass().getName());

	public DocumentoBean() {
	}

	@PostConstruct
	public void inicializar() {

		this.listaDocumentos = documentoService.findAll();
		this.listaDocumentosFiltrados = new ArrayList<>(this.listaDocumentos);
	}

	public void pegarDocumento(DocumentoDto doc) {
		
		this.documento = documentoService.findById(doc.getId());
		if (this.documento.getVendedor() != null) {
			this.idVendedorSelecionado = this.documento.getVendedor().getId();
		}
		
		this.bytesDoNovoArquivo = null;
	}

	public void pegarDocumentoSelecionado() {
		if (this.documentoSelecionado != null) {

			this.pegarDocumento(documentoSelecionado);
			
		}
	}

	public void pegarDocumentoSelecionadoPeloVendedor() {

		  // 1. Garante que o objeto está instanciado e limpo para um novo cadastro
	    this.documento = new DocumentoDto();
	    this.bytesDoNovoArquivo = null;

	    // 2. Vincula o vendedor que foi escolhido no filtro da tela principal
	    if (this.vendedorSelecionado != null) {
	        SellerDto vendedor = sellerService.findById(this.vendedorSelecionado);
	        this.documento.setVendedor(vendedor);
	        
	        // 3. Preenche também o ID usado pelo formulário de inserção e pelo método salvar()
	        this.idVendedorSelecionado = this.vendedorSelecionado;
	    }
 		
	}
	
	public void prepararNovo() {
		// Garante que o objeto está zerado e sem ID para não misturar com o editar
		this.documento = new DocumentoDto();
		this.bytesDoNovoArquivo = null;
	}

	public void pegarFileUpload(FileUploadEvent event) {
		try {
			this.bytesDoNovoArquivo = event.getFile().getContents();

			if (this.documento != null) {
				this.documento.setNomeArquivo(event.getFile().getFileName());
			}
			
			
		} catch (Exception e) {
			logger.log(Level.SEVERE, "Erro ao extrair bytes do arquivo", e);
			FacesContext.getCurrentInstance().addMessage(null, 
				new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível processar o arquivo."));
		}
	}

	public void limparFiltro() {
		this.vendedorSelecionado = null;
		this.listaDocumentosFiltrados = new ArrayList<>(this.listaDocumentos);

		}
	
	public void salvar() {
		try {
			if (this.idVendedorSelecionado != null) {
				SellerDto vend = sellerService.findById(this.idVendedorSelecionado);
				this.documento.setVendedor(vend);
			}

			// ALTERAÇÃO 3: Se houverem novos bytes na memória, joga para dentro do DTO
			if (this.bytesDoNovoArquivo != null) {
				this.documento.setConteudo(this.bytesDoNovoArquivo);
				// O nome do arquivo já foi populado dinamicamente no listener pegarFileUpload
			}

			if (this.documento.getId() == null) {
				documentoService.insert(this.documento);
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Documento salvo!"));
			} else {
				documentoService.update(this.documento);
				FacesContext.getCurrentInstance().addMessage(null, 
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Documento atualizado!")); 
			}

			// Limpeza do estado pós-salvamento bem sucedido
			this.listaDocumentos = documentoService.findAll();
			this.listaDocumentosFiltrados = listaDocumentos;
			this.documento = new DocumentoDto();
			this.idVendedorSelecionado = null;
			this.bytesDoNovoArquivo = null; 
			this.documentoSelecionado = null;

		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao salvar", exc);
			this.listaDocumentos = documentoService.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Falha", "Não foi possível atualizar devido à integridade dos dados!"));
			exc.printStackTrace();
			addErrorMessage(exc);
		} catch (Exception exc) {
			this.listaDocumentos = documentoService.findAll();
			logger.log(Level.SEVERE, "Erro ao salvar", exc);
			exc.printStackTrace();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível atualizar!"));
		}
	}

	public void filtrar() {
		
		if(this.vendedorSelecionado == null) {
			listaDocumentosFiltrados = listaDocumentos;
			
		} else {
		
			listaDocumentosFiltrados = documentoService.findBySeller(vendedorSelecionado);
		}
		
		
	}
	
	
	public void deletar(Integer id) {
		try {
			this.documento = documentoService.findById(id);
			if (this.documento != null) {

				documentoService.deleteById(id);

				this.listaDocumentos = documentoService.findAll();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Documento deletado!"));
				this.documento = new DocumentoDto();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Documento não encontrado."));
			}
		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao deletar", exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível deletar!"));
			addErrorMessage(exc);
		}
	}

	public void deletarSelecionado() {
		if (this.documentoSelecionado != null) {

			this.deletar(this.documentoSelecionado.getId());

			this.documentoSelecionado = null;

		}
	}
	
	public void tratarMudancaVendedor() {
		 if (this.vendedorSelecionado != null) {
		        // Se um vendedor foi escolhido, coloque aqui a lógica que você precisa
		        // (Nota: o botão "filtrar" já vai habilitar automaticamente por causa do disabled="#{documento.vendedorSelecionado == null}")
		        System.out.println("Vendedor selecionado: " + this.vendedorSelecionado);
		    } else {
		        // Se o usuário selecionou a opção "Vendedores" (nula)
		        System.out.println("Nenhum vendedor selecionado.");
		        }
	}

	public void habilitarLinhaSelecionada(SelectEvent event) {
		// Força o Java a atribuir o objeto vindo do evento de clique da tabela
		this.documentoSelecionado = (DocumentoDto) event.getObject();
	}

	public void desabilitarLinhaSelecionada(UnselectEvent event) {
		this.documentoSelecionado = null; // Força o Java a zerar a seleção
	}

	private void addErrorMessage(DbIntegrityException exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public List<SellerDto> getVendedoresUnicos() {
	    if (this.listaDocumentos == null) {
	        return Collections.emptyList();
	    }
	    
	    return this.listaDocumentos.stream()
	            .map(DocumentoDto::getVendedor) // Extrai o vendedor de cada documento
	            .filter(java.util.Objects::nonNull) // Evita NullPointerException se houver doc sem vendedor
	            .distinct() // Remove os repetidos (Baseado no método equals() do seu Vendedor)
	            .collect(Collectors.toList());
	}
	

	public DocumentoService getDocumentoService() {
		return documentoService;
	}

	public void setDocumentoService(DocumentoService documentoService) {
		this.documentoService = documentoService;
	}

	public DocumentoDto getDocumento() {
		return documento;
	}

	public void setDocumento(DocumentoDto documento) {
		this.documento = documento;
	}

	public List<DocumentoDto> getListaDocumentos() {
		return listaDocumentos;
	}

	public DocumentoDto getDocumentoSelecionado() {
		return documentoSelecionado;
	}

	public void setDocumentoSelecionado(DocumentoDto documentoSelecionado) {
		this.documentoSelecionado = documentoSelecionado;
	}

	public Integer getIdVendedorSelecionado() {
		return idVendedorSelecionado;
	}

	public void setIdVendedorSelecionado(Integer idVendedorSelecionado) {
		this.idVendedorSelecionado = idVendedorSelecionado;
	}


	public Integer getVendedorSelecionado() {
		return vendedorSelecionado;
	}

	public void setVendedorSelecionado(Integer vendedorSelecionado) {
		this.vendedorSelecionado = vendedorSelecionado;
	}

	public List<DocumentoDto> getListaDocumentosFiltrados() {
		return listaDocumentosFiltrados;
	}		
}
