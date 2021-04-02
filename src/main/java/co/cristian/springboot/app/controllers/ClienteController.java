package co.cristian.springboot.app.controllers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import co.cristian.springboot.app.models.entity.Cliente;
import co.cristian.springboot.app.models.service.IClienteService;
import co.cristian.springboot.app.models.service.IUploadFileService;
import co.cristian.springboot.app.util.paginator.PageRender;

@Controller
@SessionAttributes("cliente")
public class ClienteController {

	@Autowired
	private IClienteService clienteService;

	@Autowired
	private IUploadFileService uploadFileService;
	
	@Autowired
	private MessageSource messageSource;

	@GetMapping(value = { "/listar", "/" })
	public String listar(@RequestParam(name = "page", defaultValue = "0") int page, Model model,
			Authentication authentication, HttpServletRequest request, Locale locale) {

		model.addAttribute("titulo", messageSource.getMessage("text.cliente.listar.titulo", null, locale));

		Pageable pageRequest = PageRequest.of(page, 5);

		Page<Cliente> clientes = clienteService.findAll(pageRequest);

		PageRender<Cliente> pageRender = new PageRender<>("/listar", clientes);

		model.addAttribute("page", pageRender);

		model.addAttribute("clientes", clientes);

		return "listar";

	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form")
	public String crear(Map<String, Object> model) {

		Cliente cliente = new Cliente();

		model.put("titulo", "Formulario de cliente");

		model.put("cliente", cliente);

		return "form";

	}

	@Secured("ROLE_ADMIN")
	@PostMapping(value = "/form")
	public String guardar(@Valid Cliente cliente, BindingResult result, Model model,
			@RequestParam("file") MultipartFile foto, RedirectAttributes flash, SessionStatus status) {

		if (result.hasErrors()) {

			model.addAttribute("titulo", "Formulario de cliente (Campos llenados incorrectamente)");

			return "form";

		}

		if (!foto.isEmpty()) {

			if (cliente.getId() != null && cliente.getFoto() != null && cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());

			}

			String uniqueFilename = null;

			try {
				uniqueFilename = uploadFileService.copy(foto);

			} catch (IOException e) {

				e.printStackTrace();

			}

			flash.addFlashAttribute("info", "Ha subido correctamente '" + uniqueFilename + "'");

			cliente.setFoto(uniqueFilename);
		}

		String mensajeFlash = (cliente.getId() != null) ? "Cliente Editado con exito" : "Cliente Creado con exito";

		clienteService.save(cliente);

		status.setComplete();

		flash.addFlashAttribute("success", mensajeFlash);

		return "redirect:listar";

	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/form/{id}")
	public String editar(@PathVariable Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = null;

		if (id > 0) {

			cliente = clienteService.findOne(id);

			if (cliente == null) {

				flash.addFlashAttribute("error", "El ID del cliente no existe en la BBDD");

				return "redirect:/listar";

			}

		} else {

			flash.addFlashAttribute("error", "El ID del cliente no puede ser cero");

			return "redirect:/listar";

		}

		model.put("titulo", "Editar cliente");

		model.put("cliente", cliente);

		return "form";

	}

	@Secured("ROLE_ADMIN")
	@GetMapping(value = "/eliminar/{id}")
	public String eliminar(@PathVariable Long id, RedirectAttributes flash) {

		if (id > 0) {

			Cliente cliente = clienteService.findOne(id);

			clienteService.delete(id);

			flash.addFlashAttribute("success", "Cliente eliminado con exito");

			if (cliente.getId() != null && cliente.getFoto() != null && cliente.getFoto().length() > 0) {

				uploadFileService.delete(cliente.getFoto());

			}

		}

		return "redirect:/listar";

	}

	@Secured("ROLE_USER")
	@GetMapping("/ver/{id}")
	public String ver(@PathVariable Long id, Map<String, Object> model, RedirectAttributes flash) {

		Cliente cliente = clienteService.fetchByIdWithFacturas(id);

		if (cliente == null) {

			flash.addFlashAttribute("error", "El cliente no existe en la base de datos");

			return "redirect:/listar";

		}

		model.put("cliente", cliente);

		model.put("titulo", "Detalle cliente: " + cliente.getNombre());

		return "ver";

	}

	@Secured("ROLE_USER")
	@GetMapping("/uploads/{filename:.+}")
	public ResponseEntity<Resource> verFoto(@PathVariable String filename) {

		Resource recurso = null;

		try {

			recurso = uploadFileService.load(filename);

		} catch (MalformedURLException e) {

			e.printStackTrace();

		}

		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + recurso.getFilename() + "\"")
				.body(recurso);

	}

}
