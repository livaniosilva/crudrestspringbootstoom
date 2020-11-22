package br.com.stoom;


import br.com.stoom.controller.AddressController;
import br.com.stoom.entity.Address;
import br.com.stoom.service.AddressService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;


import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AddressControllerTest {

    @Autowired
    private AddressController controller;
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    @Autowired
    private AddressService service;
    

    public Address getAddress() {

        Address address = new Address();
        address.setStreetName("Rua Filipe Camarão");
        address.setNumber((long) 190);
        address.setComplement("25");
        address.setNeighbourhood("Tatuapé");
        address.setCity("São Paulo");
        address.setCountry("Brasil");
        address.setZipCode("03065000");
        address.setLatitude("-65656465456");
        address.setLongitude("94654566464");

        return address;
    }


    @Test
    public void test() throws Exception{
        assertThat(controller).isNotNull();
    }
    
    @Test
    public void findById(Long id) throws Exception{

    	when(service.findById(id)).thenReturn(getAddress());
    	this.mockMvc.perform(get("/stoom/addres/{id}")).andDo(print()).andExpect(status().isOk())
                .andExpect(content().contentType("APPLICATION.JSON"));
    }
    
}
