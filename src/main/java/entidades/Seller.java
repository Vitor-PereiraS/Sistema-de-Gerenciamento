package entidades;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import entidades.dto.SellerDto;

@Entity
public class Seller implements Serializable {

	private static final long serialVersionUID = 1L;

	private static DateTimeFormatter formatador = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seller_seq_gen")
	@SequenceGenerator(name = "seller_seq_gen", sequenceName = "seller_id", allocationSize = 1)
	private Integer id;
	private String name;
	private String email;
	private LocalDate birthDate;
	private Double baseSalary;

	@ManyToOne
	@JoinColumn(name = "departmentId", nullable = false)
	private Department department;

	@OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<Documento> documentos = new ArrayList<>();

	public Seller() {
		super();
	}

	public Seller(Integer id, String name, String email, LocalDate birthDate, Double baseSalary,
			Department department) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.baseSalary = baseSalary;
		this.department = department;
	}

	public Seller(String name, String email, LocalDate birthDate, Double baseSalary, Department department) {
		super();
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.baseSalary = baseSalary;
		this.department = department;
	}

	public Seller(SellerDto sellerDto) {
		this.id = sellerDto.getId();
		this.name = sellerDto.getName();
		this.email = sellerDto.getEmail();
		this.birthDate = sellerDto.getBirthDate();
		this.baseSalary = sellerDto.getBaseSalary();
		this.department = new Department(sellerDto.getDepartment());
	}

	public Seller(SellerDto sellerDto, Department department) {
		this.id = sellerDto.getId();
		this.name = sellerDto.getName();
		this.email = sellerDto.getEmail();
		this.birthDate = sellerDto.getBirthDate();
		this.baseSalary = sellerDto.getBaseSalary();
		this.department = department;
	}

	public void addDocumento(Documento doc) {
		this.documentos.add(doc);
	}
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}

	public Double getBaseSalary() {
		return baseSalary;
	}

	public void setBaseSalary(Double baseSalary) {
		if (baseSalary == null || baseSalary < 0) {
			throw new IllegalArgumentException("O Preço não pode ser negativo");
		}
		this.baseSalary = baseSalary;
	}

	public Department getDepartment() {
		return department;
	}

	public void setDepartment(Department department) {
		this.department = department;
	}

	
	public List<Documento> getDocumentos() {
		return documentos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Seller other = (Seller) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Seller [id=" + id + ", name=" + name + ", email=" + email + ", birthDate="
				+ birthDate.format(formatador) + ", baseSalary=" + String.format("%.2f", baseSalary) + "]";
	}
}
