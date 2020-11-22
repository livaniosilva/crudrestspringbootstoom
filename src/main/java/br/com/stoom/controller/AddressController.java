package br.com.stoom.controller;

import br.com.stoom.dto.AddressDTO;
import br.com.stoom.entity.Address;
import br.com.stoom.service.AddressService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("stoom")
public class AddressController {
    @Autowired
    AddressService service;
    @Autowired
    public AddressController(AddressService service) {
        this.service = service;
    }
    @ApiOperation(value="Retrieve all addresses")
    @GetMapping("/address")
    public ResponseEntity<List<Address>> getAll() {
        List<Address> list = service.findAll();
        if (list.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(list, HttpStatus.OK);
    }

    @ApiOperation(value="Retrieve an specific address by his Id information")
    @GetMapping("/address/{id}")
    public ResponseEntity<Address> getById(@PathVariable Long id) throws Exception {
        Address address = service.findById(id);
        if (address != null) {
            return new ResponseEntity<>(address, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value="Create a new address")
    @PostMapping("/address")
    public ResponseEntity<Address> save(@RequestBody AddressDTO addressDTO) throws Exception {
        Address address = service.save(addressDTO);
        if (address != null) {
            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(address.getId())
                    .toUri();
            return ResponseEntity.created(location).build();
        } else {
            return ResponseEntity.unprocessableEntity().build();
        }
    }

    @ApiOperation(value="Update an specific address")
    @PutMapping("/address/{id}")
    public ResponseEntity<Address> update(@PathVariable Long id, @RequestBody AddressDTO addressDTO) throws Exception {
        boolean upToDate = service.update(id, addressDTO);
        if (upToDate) {
            return new ResponseEntity<>(HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @ApiOperation(value="Delete an specific object")
    @DeleteMapping("/address/{id}")
    public ResponseEntity delete(@PathVariable Long id) throws Exception {
        boolean deletion = service.delete(id);
        if (deletion) {
            return new ResponseEntity(HttpStatus.OK);
        }else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

}
