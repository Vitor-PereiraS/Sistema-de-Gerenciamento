package entidades;

import java.io.Serializable;
import java.util.Arrays;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "DOCUMENTOS")
public class Documento implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doc_seq") // 1. APONTA para o generator do Java
	@SequenceGenerator(
	    name = "doc_seq",             // 2. DEFINE o nome do generator no Java (idêntico ao de cima)
	    sequenceName = "documentos_id", // 3. APONTA para o nome exato da SEQUENCE criada no banco ORACLE
	    allocationSize = 1
	)
	private Integer id;
	
	 @Column(name = "NOME_ARQUIVO")
	    private String nomeArquivo;

	    @Lob
	    @Column(name = "CONTEUDO_ARQUIVO")
	    private byte[] conteudo;
	    
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "ID_VENDEDOR", nullable = false)
	    private Seller vendedor;

		public Documento() {
			super();
		}

		public Documento(Integer id, String nomeArquivo, byte[] conteudo, Seller vendedor) {
			super();
			this.id = id;
			this.nomeArquivo = nomeArquivo;
			this.conteudo = conteudo;
			this.vendedor = vendedor;
		}

		public Integer getId() {
			return id;
		}

		public void setId(Integer id) {
			this.id = id;
		}

		public String getNomeArquivo() {
			return nomeArquivo;
		}

		public void setNomeArquivo(String nomeArquivo) {
			this.nomeArquivo = nomeArquivo;
		}

		public byte[] getConteudo() {
			return conteudo;
		}

		public void setConteudo(byte[] conteudo) {
			this.conteudo = conteudo;
		}

		public Seller getVendedor() {
			return vendedor;
		}

		public void setVendedor(Seller vendedor) {
			this.vendedor = vendedor;
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
			Documento other = (Documento) obj;
			if (id == null) {
				if (other.id != null)
					return false;
			} else if (!id.equals(other.id))
				return false;
			return true;
		}

		@Override
		public String toString() {
			return "Documento [id=" + id + ", nomeArquivo=" + nomeArquivo + ", conteudo=" + Arrays.toString(conteudo)
					+ ", vendedor=" + vendedor + "]";
		}
	    
	    
	
}
