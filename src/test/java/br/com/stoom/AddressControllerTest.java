package br.com.stoom;


import br.com.stoom.controller.AddressController;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
public class AddressControllerTest {

    @Autowired
    private AddressController controller;

    @Test
    public void test() throws Exception{
        assertThat(controller).isNotNull();
    }
}
