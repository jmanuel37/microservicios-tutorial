package com.usuarioservice.servicio;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.usuarioservice.entidades.Usuario;
import com.usuarioservice.feignclients.CarroFeignClient;
import com.usuarioservice.feignclients.MotoFeignClient;
import com.usuarioservice.modelos.Carro;
import com.usuarioservice.modelos.Moto;
import com.usuarioservice.repositorio.UsuarioRepository;

@Service
public class UsuarioService {
	
	@Autowired
	public RestTemplate resTemplate ;
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	/**** Template **********************************/
	public  List<Carro> getCarros(int usuarioId){
		List<Carro> carros=resTemplate.getForObject("http://localhost:8002/carro/usuario/"+usuarioId, List.class);
	    return carros;	
	}
	
	public  List<Moto> getMotos(int usuarioId){
		List<Moto> moto=resTemplate.getForObject("http://localhost:8003/moto/usuario/"+usuarioId, List.class);
	    return moto;	
	}
	
	/********************************************/
	/*****FeingnClient***************************/
	@Autowired
	private CarroFeignClient carroFeignClient;
	public Carro saveCarro(int usuarioId,Carro carro) {
		carro.setUsuarioId(usuarioId);
		Carro nuevoCarro =carroFeignClient.save(carro);
		return nuevoCarro;
	}
	
	@Autowired
	private MotoFeignClient motoFeignClient;
	public Moto saveMoto(int usuarioId,Moto moto) {
		moto.setUsuarioId(usuarioId);
		Moto nuevaMoto =motoFeignClient.save(moto);
		return nuevaMoto;
	}
	
	
	public Map<String, Object> getUsuarioAndVehiculos(int usuarioId){
		Map<String, Object> resultado =new HashMap<>();
		Usuario usuario=usuarioRepository.findById(usuarioId).orElse(null);
		if(usuario == null) {
			resultado.put("Mensaje","El Usuario no Existe");
			return resultado;
		}
		
		resultado.put("Usuario",usuario);
		List<Carro> carros=carroFeignClient.getCarros(usuarioId);
		if(carros.isEmpty()) {
			resultado.put("Carro","El usuario no tiene carros");
		}else {
			resultado.put("Carros",carros);
		}
		
		List<Moto> motos=motoFeignClient.getMotos(usuarioId);
		if(motos.isEmpty()) {
			resultado.put("Motos","El usuario no tiene motos");
		}else {
			resultado.put("Motos",motos);
		}
		return resultado;
		
	}
	/**********************************************/
	public List<Usuario> getAll(){
		return usuarioRepository.findAll();
	}
	
	public Usuario getUsuarioById(int id) {
		return usuarioRepository.findById(id).orElse(null);
		
	}
	public Usuario save(Usuario usuario) {
		Usuario nuevoUsuario=usuarioRepository.save(usuario);
		return nuevoUsuario;
	}
}
