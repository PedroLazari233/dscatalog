package com.pedrolazari.dscatalog.services;

import com.pedrolazari.dscatalog.dto.*;
import com.pedrolazari.dscatalog.dto.UserDTO;
import com.pedrolazari.dscatalog.entities.Category;
import com.pedrolazari.dscatalog.entities.Role;
import com.pedrolazari.dscatalog.entities.User;
import com.pedrolazari.dscatalog.entities.User;
import com.pedrolazari.dscatalog.repositories.CategoryRepository;
import com.pedrolazari.dscatalog.repositories.RoleRepository;
import com.pedrolazari.dscatalog.repositories.UserRepository;
import com.pedrolazari.dscatalog.repositories.UserRepository;
import com.pedrolazari.dscatalog.services.exceptions.DataBaseException;
import com.pedrolazari.dscatalog.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        Page<User> users = userRepository.findAll(pageable);
        return users.map( user -> new UserDTO(user));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> userOptional = userRepository.findById(id);
        User user = userOptional.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO insertUser(UserInsertDTO userInsertDTO) {
        User user = new User();
        copyDtoToEntity(userInsertDTO, user);
        user.setPassword(bCryptPasswordEncoder.encode(userInsertDTO.getPassword()));
        user = userRepository.save(user);
        return new UserDTO(user);
    }

    @Transactional
    public UserDTO updateUser(Long id, UserDTO userDTO) {
        try{
            User user = userRepository.getOne(id);
            copyDtoToEntity(userDTO, user);
            user = userRepository.save(user);
            return new UserDTO(user);
        }
        catch (EntityNotFoundException e){
            throw new ResourceNotFoundException("Id not found " + id);
        }
    }

    public void deleteUser(Long id) {
        try{
            userRepository.deleteById(id);
        }
        catch (EmptyResultDataAccessException e)
        {
            throw new ResourceNotFoundException("Id not found " + id);
        }
        catch (DataIntegrityViolationException e)
        {
            throw new DataBaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(UserDTO userDTO, User user) {

        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmail());

        user.getRoles().clear();
        for (RoleDTO roleDTO : userDTO.getRoleDTOS()) {
            Role role = roleRepository.getOne(roleDTO.getId());
            user.getRoles().add(role);
        }
    }
}
