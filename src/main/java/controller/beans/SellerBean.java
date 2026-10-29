package controller.beans;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.UploadedFile;

import entidades.dto.DepartmentDto;
import entidades.dto.DocumentoDto;
import entidades.dto.SellerDto;
import exception.DbIntegrityException;
import service.DepartmentService;
import service.DocumentoService;
import service.SellerService;

@Named(value = "seller")
@ViewScoped
public class SellerBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private SellerService sellerService;
	@Inject
	private DepartmentService departmentService;
	
	@Inject
	private DocumentoService documentoService;
	
	private SellerDto novoSeller = new SellerDto();
	private SellerDto vendedorSelecionado;

	private Integer idDepartamentoSelecionado;

	private List<SellerDto> listaVendedores;
	private List<SellerDto> vendedoresFiltrados;
	private String campoFiltro = "NOME"; // Valor inicial padrão
	private String valorFiltro;
	private String departamentoFiltro;

	private String tituloDialog;

	private Double salarioFiltroMax;
	private Double salarioFiltroMin;

	private LocalDate dataInicio = null;
	private LocalDate dataFim = null;

	private UploadedFile arquivoUploaded;

	private Logger logger = Logger.getLogger(getClass().getName());

	public SellerBean() {

	}

	@PostConstruct
	public void inicializar() {

		this.listaVendedores = sellerService.findAll();
		this.vendedoresFiltrados = new ArrayList<>(this.listaVendedores);
	}

	public void pegarVendedor(SellerDto vendedorSelecionado) {
		this.tituloDialog = "Editar Vendedor";
		this.novoSeller = sellerService.findById(vendedorSelecionado.getId());
		if (this.novoSeller.getDepartment() != null) {
			this.idDepartamentoSelecionado = this.novoSeller.getDepartment().getId();
		}
	}

	public void pegarVendedorSelecionado() {
		if (this.vendedorSelecionado != null) {
			// Reutiliza a lógica que você já tinha passando o objeto selecionado
			this.pegarVendedor(this.vendedorSelecionado);
		}
	}

	public void prepararNovo() {
		// Garante que o objeto está zerado e sem ID para não misturar com o editar
		this.tituloDialog = "Adicionar Vendedor";
		this.novoSeller = new SellerDto();
	}

	public void filtrar() {
		// Se não digitou nada, volta a exibir a lista completa
		// if (valorFiltro == null || valorFiltro.trim().isEmpty()) {
		// this.vendedoresFiltrados = new ArrayList<>(this.listaVendedores);
		// return;
		// }

		// String busca = valorFiltro.toLowerCase().trim();
		// this.vendedoresFiltrados = new ArrayList<>();

		// Varre a lista original vinda do banco aplicando o filtro selecionado
		/*
		 * for (SellerDto vend : this.listaVendedores) { if ("NOME".equals(campoFiltro)
		 * && vend.getName() != null && vend.getName().toLowerCase().contains(busca)) {
		 * this.vendedoresFiltrados.add(vend); } else if ("EMAIL".equals(campoFiltro) &&
		 * vend.getEmail() != null && vend.getEmail().toLowerCase().contains(busca)) {
		 * this.vendedoresFiltrados.add(vend); } else if
		 * ("DEPARTAMENTO".equals(campoFiltro) && vend.getDepartment() != null &&
		 * vend.getDepartment().getName() != null &&
		 * vend.getDepartment().getName().toLowerCase().contains(busca)) {
		 * this.vendedoresFiltrados.add(vend); } }
		 */

		this.vendedoresFiltrados = new ArrayList<>();
		// Preparar variáveis de busca de forma segura
		String buscaTexto = (valorFiltro != null) ? valorFiltro.toLowerCase().trim() : "";
		Double buscaValorMin;
		Double buscaValorMax;

		LocalDate buscaDataInicio = (dataInicio != null) ? dataInicio : LocalDate.MIN;
		LocalDate buscaDataFim = (dataFim != null) ? dataFim : LocalDate.MAX;

		if (salarioFiltroMin == null) {
			buscaValorMin = 0.0;
		} else {
			buscaValorMin = salarioFiltroMin;
		}
		if (salarioFiltroMax == null) {
			buscaValorMax = Double.MAX_VALUE;
		} else {
			buscaValorMax = salarioFiltroMax;
		}

		// Varre a lista completa aplicando as duas condições de filtro ao mesmo tempo
		for (SellerDto vend : this.listaVendedores) {

			// 1. REGRA DO FILTRO DE TEXTO (Nome ou Email)
			boolean atendeTexto = false;

			if (buscaTexto.isEmpty()) {
				atendeTexto = true; // Se não digitou nada, passa direto por esse filtro
			} else if ("NOME".equals(campoFiltro) && vend.getName() != null) {
				atendeTexto = vend.getName().toLowerCase().contains(buscaTexto);
			} else if ("EMAIL".equals(campoFiltro) && vend.getEmail() != null) {
				atendeTexto = vend.getEmail().toLowerCase().contains(buscaTexto);
			}

			// 2. REGRA DO FILTRO DE DEPARTAMENTO (Dropdown)
			boolean atendeDepartamento = false;

			// Se 'departamentoFiltro' for nulo (opção "Todos os Departamentos"), passa
			// direto
			if (this.departamentoFiltro == null) {
				atendeDepartamento = true;
			} else if (vend.getDepartment() != null && vend.getDepartment().getName() != null) {
				// Comparação exata ignorando maiúsculas/minúsculas
				atendeDepartamento = vend.getDepartment().getName().equalsIgnoreCase(this.departamentoFiltro);
			}

			// 3. REGRA DO FILTRO DE Salario(Dropdown)
			boolean atendeSalario = false;

			if (buscaValorMax.compareTo(buscaValorMin) < 0) {

				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Falha", "O valor máximo não pode ser menor que o valor mínimo."));

				return;
			}

			if (vend.getBaseSalary() != null) {
				atendeSalario = vend.getBaseSalary() >= buscaValorMin && vend.getBaseSalary() <= buscaValorMax;
			}

			boolean atendeDatas = false;

			if (dataInicio == null || dataFim == null) {
				atendeDatas = true; // Passa direto por esse filtro
			} else if (buscaDataInicio.compareTo(dataFim) > 0) {
				FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
						"Falha", "A Data de inicio deve ser anterior a data de fim."));

				return;
			}

			if (vend.getBirthDate() != null) {
				// atendeDatas = vend.getBirthDate().compareTo(buscaDataInicio) >= 0 &&
				// vend.getBirthDate().compareTo(buscaDataFim) <= 0 ;

				LocalDate dataVend = vend.getBirthDate();
				atendeDatas = (!dataVend.isBefore(buscaDataInicio)) && (!dataVend.isAfter(buscaDataFim));
			}

			// 3. COMBINAÇÃO DOS DOIS FILTROS
			// O vendedor só entra na lista se passar nas duas validações simultaneamente
			if (atendeTexto && atendeDepartamento && atendeSalario && atendeDatas) {
				this.vendedoresFiltrados.add(vend);
			}
		}
	}

	// 4. Método para limpar a pesquisa
	public void limparFiltro() {
		this.valorFiltro = null;
		this.campoFiltro = "NOME";
		this.departamentoFiltro = null;
		this.salarioFiltroMin = null;
		this.salarioFiltroMax = null;
		this.dataInicio = null;
		this.dataFim = null;
		this.vendedoresFiltrados = new ArrayList<>(this.listaVendedores); // Restaura todos
	}

	public void salvar() {
		try {
			if (this.idDepartamentoSelecionado != null) {
				DepartmentDto depto = departmentService.findById(this.idDepartamentoSelecionado);
				this.novoSeller.setDepartment(depto);
			}

			
			/*
			if (arquivoUploaded != null && arquivoUploaded.getFileName() != null
					&& !arquivoUploaded.getFileName().isEmpty()) {

				// 2. Instancia a entidade Documento
				DocumentoDto doc = new DocumentoDto();
				doc.setNomeArquivo(arquivoUploaded.getFileName());
				doc.setConteudo(arquivoUploaded.getContents()); // Transforma o upload em byte[] para o Oracle

				// 3. Amarra o relacionamento nas duas direções
				doc.setVendedor(novoSeller); // Aponta o pai no filho
				novoSeller.getDocumentos().add(doc); // Adiciona o filho na lista do pai
			}
*/
			if (this.novoSeller.getId() == null) {

				System.out.println("Vendedor");
				sellerService.insert(this.novoSeller);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vendedor salvo!"));
			} else {
				sellerService.update(this.novoSeller);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vendedor atualizado!"));
			}

			// Recarrega a lista do banco para atualizar a tabela na tela
			// RequestContext.getCurrentInstance().update(":formPrincipal");

			this.listaVendedores = sellerService.findAll();
			filtrar();
			this.novoSeller = new SellerDto();
			this.idDepartamentoSelecionado = null;

		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao salvar", exc);

			// Recarrega a lista do banco para atualizar a tabela na tela
			this.listaVendedores = sellerService.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Falha", "Não foi possível atualizar!"));
			addErrorMessage(exc);
		} catch (Exception exc) {
			// Recarrega a lista do banco para atualizar a tabela na tela
			this.listaVendedores = sellerService.findAll();
			logger.log(Level.SEVERE, "Erro ao salvar", exc);
			// addErrorMessage(exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível atualizar!"));
		}
	}

	public void incluirArquivo() {
		
		try {
			System.out.println(arquivoUploaded);
		if (arquivoUploaded != null && arquivoUploaded.getFileName() != null
				&& !arquivoUploaded.getFileName().isEmpty()) {

			// 2. Instancia a entidade Documento
			DocumentoDto doc = new DocumentoDto();
			doc.setNomeArquivo(this.arquivoUploaded.getFileName());
			doc.setConteudo(this.arquivoUploaded.getContents()); // Transforma o upload em byte[] para o Oracle

			// 3. Amarra o relacionamento nas duas direções
			doc.setVendedor(this.novoSeller); // Aponta o pai no filho
			novoSeller.getDocumentos().add(doc); // Adiciona o filho na lista do pai
			documentoService.insert(doc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Documento adicionado!"));
			}
		System.out.println("Erro " + arquivoUploaded);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível adicionar o arquivo!"));
		}
	}
	
	public void deletar(Integer id) {
		try {
			this.novoSeller = sellerService.findById(id);
			if (this.novoSeller != null) {

				sellerService.deleteById(id);

				this.listaVendedores = sellerService.findAll();
				filtrar();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Vendedor deletado!"));
				this.novoSeller = new SellerDto();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Vendedor não encontrado."));
			}
		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao deletar", exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível deletar!"));
			addErrorMessage(exc);
		}
	}

	public void deletarSelecionado() {
		if (this.vendedorSelecionado != null) {
			// 1. Reutiliza o método deletar que você já tem criado, passando o ID do
			// selecionado
			this.deletar(this.vendedorSelecionado.getId());

			// 2. IMPORTANTE: Limpa a variável após deletar para o botão desabilitar
			// novamente
			this.vendedorSelecionado = null;

			// 3. (Opcional) Se o seu método filtrar() atualizar a lista, você pode chamá-lo
			// aqui
			// this.filtrar();
		}
	}

	public String novoVendedor() {
		this.novoSeller = new SellerDto();

		return "add-vendedor?faces-redirect=true";
	}

	public void habilitarLinhaSelecionada(SelectEvent event) {
		// Força o Java a atribuir o objeto vindo do evento de clique da tabela
		this.vendedorSelecionado = (SellerDto) event.getObject();
	}

	public void desabilitarLinhaSelecionada(UnselectEvent event) {
		this.vendedorSelecionado = null; // Força o Java a zerar a seleção
	}

	private void addErrorMessage(DbIntegrityException exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<SellerDto> getListaVendedors() {
		return listaVendedores;
	}


	public void setListaVendedores(List<SellerDto> listaVendedores) {
		this.listaVendedores = listaVendedores;
	}

	public SellerDto getNovoSeller() {
		return novoSeller;
	}

	public void setNovoSeller(SellerDto novoSeller) {
		this.novoSeller = novoSeller;
	}

	public Integer getIdDepartamentoSelecionado() {
		return idDepartamentoSelecionado;
	}

	public void setIdDepartamentoSelecionado(Integer idDepartamentoSelecionado) {
		this.idDepartamentoSelecionado = idDepartamentoSelecionado;
	}

	public List<SellerDto> getVendedoresFiltrados() {
		return vendedoresFiltrados;
	}

	public void setVendedoresFiltrados(List<SellerDto> vendedoresFiltrados) {
		this.vendedoresFiltrados = vendedoresFiltrados;
	}

	public String getCampoFiltro() {
		return campoFiltro;
	}

	public void setCampoFiltro(String campoFiltro) {
		this.campoFiltro = campoFiltro;
	}

	public String getValorFiltro() {
		return valorFiltro;
	}

	public void setValorFiltro(String valorFiltro) {
		this.valorFiltro = valorFiltro;
	}

	public String getDepartamentoFiltro() {
		return departamentoFiltro;
	}

	public void setDepartamentoFiltro(String departamentoFiltro) {
		this.departamentoFiltro = departamentoFiltro;
	}

	public Double getSalarioFiltroMax() {
		return salarioFiltroMax;
	}

	public void setSalarioFiltroMax(Double salarioFiltroMax) {
		this.salarioFiltroMax = salarioFiltroMax;
	}

	public Double getSalarioFiltroMin() {
		return salarioFiltroMin;
	}

	public void setSalarioFiltroMin(Double salarioFiltroMin) {
		this.salarioFiltroMin = salarioFiltroMin;
	}

	public SellerDto getVendedorSelecionado() {
		return vendedorSelecionado;
	}

	public void setVendedorSelecionado(SellerDto vendedorSelecionado) {
		this.vendedorSelecionado = vendedorSelecionado;
	}

	public String getTituloDialog() {
		return tituloDialog;
	}

	public void setTituloDialog(String tituloDialog) {
		this.tituloDialog = tituloDialog;
	}

	public LocalDate getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(LocalDate dataInicio) {
		this.dataInicio = dataInicio;
	}

	public LocalDate getDataFim() {
		return dataFim;
	}

	public void setDataFim(LocalDate dataFim) {
		this.dataFim = dataFim;
	}

	public UploadedFile getArquivoUploaded() {
		return arquivoUploaded;
	}

	public void setArquivoUploaded(UploadedFile arquivoUploaded) {
		this.arquivoUploaded = arquivoUploaded;
	}

}
