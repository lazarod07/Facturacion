package co.cristian.springboot.app.models.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import co.cristian.springboot.app.models.dao.IClienteDao;
import co.cristian.springboot.app.models.dao.IFacturaDao;
import co.cristian.springboot.app.models.dao.IProductoDao;
import co.cristian.springboot.app.models.entity.Cliente;
import co.cristian.springboot.app.models.entity.Factura;
import co.cristian.springboot.app.models.entity.Producto;

@Service
public class ClienteServiceImpl implements IClienteService {
	
	@Autowired
	private IClienteDao clienteDAO;
	
	@Autowired
	private IProductoDao productoDAO;
	
	@Autowired
	private IFacturaDao facturaDao;
	
	@Override
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		
		return (List<Cliente>) clienteDAO.findAll();
		
	}
	
	@Override
	@Transactional
	public void save(Cliente cliente) {
		
		clienteDAO.save(cliente);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente findOne(Long id) {
		
		return clienteDAO.findById(id).get();
		
	}

	@Override
	@Transactional
	public void delete(Long id) {
		
		clienteDAO.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pageable) {
		
		return clienteDAO.findAll(pageable);
		
	}

	@Override
	@Transactional(readOnly = true)
	public List<Producto> findByNombre(String term) {
		
		return productoDAO.findByNombre(term);
		
	}

	@Override
	@Transactional
	public void saveFactura(Factura factura) {
		
		facturaDao.save(factura);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Producto findProductoById(Long id) {
		
		return productoDAO.findById(id).get();
		
	}

	@Override
	@Transactional(readOnly = true)
	public Factura findFacturaById(Long id) {
		
		return facturaDao.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void deleteFactura(Long id) {
		
		facturaDao.deleteById(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Factura fetchByIdWithClienteWithItemFacturaWithProducto(Long id) {
		
		return facturaDao.fetchByIdWithClienteWithItemFacturaWithProducto(id);
		
	}

	@Override
	@Transactional(readOnly = true)
	public Cliente fetchByIdWithFacturas(Long id) {
		
		return clienteDAO.fetchByIdWithFacturas(id);
		
	}

}
