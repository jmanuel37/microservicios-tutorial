package com.usuarioservice.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.usuarioservice.entidades.Usuario;

@Repository
public interface  UsuarioRepository extends JpaRepository<Usuario,Integer> {

}
