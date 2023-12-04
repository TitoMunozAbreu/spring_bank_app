package com.example.services;

import com.example.models.CustomerEntity;
import com.example.models.enums.Role;
import com.example.models.request.CustomerEditRequest;
import com.example.models.response.AccountResponse;
import com.example.models.response.CustomerResponse;
import com.example.models.response.CustomerUserResponse;
import com.example.models.response.OperationResponse;
import com.example.repositories.AccountRepository;
import com.example.repositories.CustomerRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CustomerService implements UserDetailsService {
    private CustomerRepository customerRepository;
    private AccountRepository accountRepository;
    private PasswordEncoder encoder;

    public CustomerService(CustomerRepository customerRepository, AccountRepository accountRepository, PasswordEncoder encoder) {
        this.customerRepository = customerRepository;
        this.accountRepository = accountRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        return this.customerRepository.findByEmail(email).orElseThrow(
                () -> new UsernameNotFoundException("User not found"));
    }
    @Transactional
    public void create(CustomerEntity customer) {
        customer.setPassword(encoder.encode(customer.getPassword()));
        this.customerRepository.save(customer);
    }

    @Transactional
    public List<CustomerResponse> findAll() {
        return toCustomerResponse(this.customerRepository.findAll());

    }

    @Transactional
    public CustomerEntity findById(Integer id) {
        return customerRepository.findById(id).get();
    }





    @Transactional
    public void deleteById(Integer id){
        customerRepository.deleteById(id);
    }

    public void editById(Integer id, CustomerEntity editCustomer) {
        //buscar customer segun ID
        CustomerEntity customerFound = customerRepository.findById(id).get();
        //Editar atributos del customerFound
        customerFound.setDni(editCustomer.getDni());
        customerFound.setName(editCustomer.getName());
        customerFound.setLastName(editCustomer.getLastName());
        customerFound.setDob(editCustomer.getDob());
        customerFound.setEmail(editCustomer.getEmail());
        customerFound.setMobile(editCustomer.getMobile());
        //almacenar customer con los cambios realizados
        customerRepository.save(customerFound);

    }
    private List<CustomerResponse> toCustomerResponse(List<CustomerEntity> customers) {
        return customers.stream()
                .filter(customer -> customer.getRole().equals(Role.USER))
                .map(customer -> new CustomerResponse(
                            customer.getId(),
                            customer.getName(),
                            customer.getLastName(),
                            customer.getEmail(),
                            customer.getMobile()
                            ))
                .collect(Collectors.toList());
    }


    public Optional<CustomerEntity> findByEmail(String email) {
        return  this.customerRepository.findByEmail(email);
    }
}
