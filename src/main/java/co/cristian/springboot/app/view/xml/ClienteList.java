package co.cristian.springboot.app.view.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import co.cristian.springboot.app.models.entity.Cliente;

@XmlRootElement(name = "clientes")
public class ClienteList {

	@XmlElement(name = "cliente")
	public List<Cliente> clientes;

	public List<Cliente> getClientes() {
		return clientes;
	}

	public ClienteList(List<Cliente> clientes) {
		this.clientes = clientes;
	}

	public ClienteList() {

	}

}
