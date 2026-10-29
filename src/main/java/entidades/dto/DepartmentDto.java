package entidades.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import entidades.Department;
import entidades.Seller;

public class DepartmentDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String name;
	
	private List<SellerDto> sellers = new ArrayList<>();

	public DepartmentDto(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}
	
	public DepartmentDto() {
		// TODO Auto-generated constructor stub
	}

	public DepartmentDto(Department dep) {
		this.id = dep.getId();
		this.name = dep.getName();
	
		for(Seller seller : dep.getSellers()) {
		
			sellers.add(new SellerDto(seller, this));
		}
				
	}
	
	  public DepartmentDto(Department dep, boolean carregarVendedores) {
	        this.id = dep.getId();
	        this.name = dep.getName();
	        // Se for falso, ele não entra no loop e a recursão para aqui!
	        if (carregarVendedores && dep.getSellers() != null) {
	            for(Seller seller : dep.getSellers()) {
	                this.sellers.add(new SellerDto(seller));
	            }
	        }
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


	public List<SellerDto> getSellerDto() {
		return sellers;
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
		DepartmentDto other = (DepartmentDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Department: "+ name;
	}
	
	
}
