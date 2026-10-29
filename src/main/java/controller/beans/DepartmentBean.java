package controller.beans;

import java.io.Serializable;
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

import entidades.dto.DepartmentDto;
import exception.DbIntegrityException;
import service.DepartmentService;

@Named(value = "department")
@ViewScoped
public class DepartmentBean implements Serializable {

	private static final long serialVersionUID = 1L;
	@Inject
	private DepartmentService departmentService;
	private DepartmentDto novoDepartment = new DepartmentDto();
	private DepartmentDto departamentoSelecionado;

	private List<DepartmentDto> listaDepartamentos;
	private Logger logger = Logger.getLogger(getClass().getName());

	public DepartmentBean() {

	}

	@PostConstruct
	public void inicializar() {

		this.listaDepartamentos = departmentService.findAll();

	}

	/*
	 * testando nova forma public void pegarDepartamento(DepartmentDto
	 * departamentoSelecionado) { this.novoDepartment =
	 * departmentService.findById(departamentoSelecionado.getId()); }
	 * 
	 * public void pegarDepartamentoSelecionado() { if(this.departamentoSelecionado
	 * != null) { this.pegarDepartamento(this.departamentoSelecionado); } }
	 */

	
	public void habilitarLinhaSelecionada(SelectEvent event) {
		this.departamentoSelecionado = (DepartmentDto) event.getObject();
	    
	}
	
	public void desabilitarLinhaSelecionada(UnselectEvent event) {
	    this.departamentoSelecionado  = null;
	}

	public void prepararNovo() {
		// Garante que o objeto está zerado e sem ID para não misturar com o editar
		this.novoDepartment = new DepartmentDto();
	}

	public String findById(Integer id) {

		logger.info("Carregando estudante");

		try {
			departmentService.findById(id);

		} catch (DbIntegrityException exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erro ao carregar Estudantes", exc);

			// add error message for JSF page
			addErrorMessage(exc);
		}

		return null;
	}

	public void salvar() {
		try {
			if (this.novoDepartment.getId() == null) {
				// Em vez de chamar o insert(dep) que redireciona, faça o insert direto aqui

				System.out.println("Departamento");
				departmentService.insert(this.novoDepartment);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Departamento salvo!"));
			} else {
				departmentService.update(this.novoDepartment);
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Departamento atualizado!"));
			}

			// Recarrega a lista do banco para atualizar a tabela na tela
			this.listaDepartamentos = departmentService.findAll();
			// Reseta o DTO para o formulário ficar limpo
			this.novoDepartment = new DepartmentDto();

		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao salvar", exc);

			// Recarrega a lista do banco para atualizar a tabela na tela
			this.listaDepartamentos = departmentService.findAll();
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Falha", "Não foi possível atualizar!"));
			addErrorMessage(exc);
		} catch (Exception exc) {
			// Recarrega a lista do banco para atualizar a tabela na tela
			this.listaDepartamentos = departmentService.findAll();
			logger.log(Level.SEVERE, "Erro ao salvar", exc);
			// addErrorMessage(exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível atualizar!"));
		}
	}

	/* antigo
	public void deletar(Integer id) {
		try {
			this.novoDepartment = departmentService.findById(id);
			if (this.novoDepartment != null) {

				if (this.novoDepartment.getSellerDto() != null && !this.novoDepartment.getSellerDto().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Aviso", "Não é possível excluir! Este departamento possui vendedores associados."));
					return; // Para a execução aqui e não deleta nada
				}

				departmentService.deleteById(id);

				this.listaDepartamentos = departmentService.findAll();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Departamento deletado!"));
				this.novoDepartment = new DepartmentDto();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Departamento não encontrado."));
			}
		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao deletar", exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível deletar!"));
			addErrorMessage(exc);
		}
	}
*/
	
	public void pegarDepartamento() {
		if (this.departamentoSelecionado != null) {
			this.novoDepartment = departmentService.findById(this.departamentoSelecionado.getId());
		}
	}
	public void deletar() {
		try {
			 this.pegarDepartamento();
			if (this.novoDepartment != null) {

				if (this.novoDepartment.getSellerDto() != null && !this.novoDepartment.getSellerDto().isEmpty()) {
					FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,
							"Aviso", "Não é possível excluir! Este departamento possui vendedores associados."));
					return; // Para a execução aqui e não deleta nada
				}

				departmentService.deleteById(this.novoDepartment.getId());

				this.listaDepartamentos = departmentService.findAll();

				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Departamento deletado!"));
				this.novoDepartment = new DepartmentDto();
			} else {
				FacesContext.getCurrentInstance().addMessage(null,
						new FacesMessage(FacesMessage.SEVERITY_WARN, "Aviso", "Departamento não encontrado."));
			}
		} catch (DbIntegrityException exc) {
			logger.log(Level.SEVERE, "Erro ao deletar", exc);
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Falha", "Não foi possível deletar!"));
			addErrorMessage(exc);
		}
	}
	
	public String insert(DepartmentDto dep) {

		try {
			departmentService.insert(dep);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Sucesso", "Departamento salvo!"));

			// Limpa o objeto do bean para resetar o formulário na tela
			this.novoDepartment = new DepartmentDto();
		} catch (DbIntegrityException exc) {
			// send this to server logs
			logger.log(Level.SEVERE, "Erro ao carregar Estudantes", exc);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro", "Erro ao salvar."));
			// add error message for JSF page
			addErrorMessage(exc);
		}

		return "lista-departamentos?faces-redirect=true";
	}

	public String novoDepartamento() {
		this.novoDepartment = new DepartmentDto();

		return "add-departamento?faces-redirect=true";
	}

	private void addErrorMessage(DbIntegrityException exc) {
		FacesMessage message = new FacesMessage("Error: " + exc.getMessage());
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<DepartmentDto> getListaDepartamentos() {
		return listaDepartamentos;
	}

	public void setListaDepartamentos(List<DepartmentDto> listaDepartamentos) {
		this.listaDepartamentos = listaDepartamentos;
	}

	public DepartmentDto getNovoDepartment() {
		return novoDepartment;
	}

	public void setNovoDepartment(DepartmentDto novoDepartment) {
		this.novoDepartment = novoDepartment;
	}

	public DepartmentDto getDepartamentoSelecionado() {
		return departamentoSelecionado;
	}

	public void setDepartamentoSelecionado(DepartmentDto departamentoSelecionado) {
		this.departamentoSelecionado = departamentoSelecionado;
	}

}
