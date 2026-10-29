package entidades.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import entidades.Documento;
import entidades.Seller;

public class DocumentoDto implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String nomeArquivo;
	private byte[] conteudo;
	
	private SellerDto vendedor;

	public DocumentoDto() {
		super();
	}

	public DocumentoDto(Integer id, String nomeArquivo, byte[] conteudo, SellerDto vendedor) {
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

	public SellerDto getVendedor() {
		return vendedor;
	}

	public void setVendedor(SellerDto vendedor) {
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
		DocumentoDto other = (DocumentoDto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "DocumentoDto [id=" + id + ", nomeArquivo=" + nomeArquivo + ", conteudo=" + Arrays.toString(conteudo)
				+ ", vendedor=" + vendedor + "]";
	}



}
