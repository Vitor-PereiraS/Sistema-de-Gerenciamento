package entidades.dto;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import entidades.Seller;

public class SellerDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private Integer id;
	private String name;
	private String email;
	private LocalDate birthDate;
	private Double baseSalary;
	private DepartmentDto department;
	
	private List<DocumentoDto> documentos = new ArrayList<>(); 
	
	public void setId(Integer id) {
		this.id = id;
	}


	public void setName(String name) {
		this.name = name;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public void setBirthDate(LocalDate birthDate) {
		this.birthDate = birthDate;
	}


	public void setBaseSalary(Double baseSalary) {
		this.baseSalary = baseSalary;
	}


	public void setDepartment(DepartmentDto department) {
		this.department = department;
	}


	public SellerDto(Integer id, String name, String email, LocalDate birthDate, Double baseSalary,
			DepartmentDto department) {
		super();
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthDate = birthDate;
		this.baseSalary = baseSalary;
		this.department = department;
	}
	
	
	public SellerDto() {
		// TODO Auto-generated constructor stub
	}

	

	public SellerDto(Seller seller) {
		this.id = seller.getId();
		this.name = seller.getName();
		this.email = seller.getEmail();
		this.birthDate = seller.getBirthDate();
		this.baseSalary = seller.getBaseSalary();
		
		this.department = new DepartmentDto(seller.getDepartment(), false);

	}
	
	public SellerDto(Seller seller, DepartmentDto departmentDto) {
		this.id = seller.getId();
		this.name = seller.getName();
		this.email = seller.getEmail();
		this.birthDate = seller.getBirthDate();
		this.baseSalary = seller.getBaseSalary();
		
		this.department = departmentDto;
	}


	public Integer getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public LocalDate getBirthDate() {
		return birthDate;
	}

	public Double getBaseSalary() {
		return baseSalary;
	}

	public DepartmentDto getDepartment() {
		return department;
	}

	
	public List<DocumentoDto> getDocumentos() {
		return documentos;
	}

	public String getDataFormatada() {
		if(this.birthDate == null) {
			return "";
		}
		return this.birthDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}

	  public void setBirthDateFormatted(String birthDateStr) {
	        if (birthDateStr == null || birthDateStr.trim().isEmpty()) {
	            this.birthDate = null;
	        } else {
	            // Trata se o componente enviar no formato ISO (yyyy-MM-dd) ou brasileiro (dd/MM/yyyy)
	            if (birthDateStr.contains("-")) {
	                this.birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
	            } else {
	                this.birthDate = LocalDate.parse(birthDateStr, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            }
	        }
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
		SellerDto other = (SellerDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		return "Seller id: " + id + ", name: " + name + ", email: " + email + ", birthDate: " + birthDate
				+ ", baseSalary: " + baseSalary + ", " + department;
	}


}
