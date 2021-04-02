package co.cristian.springboot.app.models.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import co.cristian.springboot.app.models.entity.Usuario;

public interface IUsuarioDao extends JpaRepository<Usuario, Long>{
	
	public Usuario findByUsername(String username);

}
