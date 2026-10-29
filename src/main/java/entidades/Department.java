package entidades;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;

import entidades.dto.DepartmentDto;
import entidades.dto.SellerDto;

@Entity
public class Department implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "department_seq_gen")
	@SequenceGenerator(name = "department_seq_gen", sequenceName = "department_id", allocationSize = 1)
	private Integer id;
	private String name;

	@OneToMany(mappedBy = "department")
	List<Seller> sellers = new ArrayList<>();

	public Department() {
		super();
	}

	public Department(Integer id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	public Department(DepartmentDto dep) {

		this.id = dep.getId();
		this.name = dep.getName();

		for (SellerDto sellerDto : dep.getSellerDto()) {
			this.sellers.add(new Seller(sellerDto, this));
		
		}
	}
	
	  public Department(DepartmentDto dep, boolean carregarVendedores) {
	        this.id = dep.getId();
	        this.name = dep.getName();
	        // Se for falso, ele não entra no loop e a recursão para aqui!
	        if (carregarVendedores && dep.getSellerDto() != null) {
	            for(SellerDto seller : dep.getSellerDto()) {
	                this.sellers.add(new Seller(seller));
	            }
	        }
	    }

	public Department(String name) {
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(int i) {
		this.id = i;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<Seller> getSellers() {
		return sellers;
	}

	public void addSeller(Seller seller) {
		sellers.add(seller);
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
		Department other = (Department) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Department [id=" + id + ", name=" + name + "]";
	}

}
