package br.com.stoom.service;

import br.com.stoom.dto.AddressDTO;
import br.com.stoom.entity.Address;
import br.com.stoom.repository.AddressRepository;
import javassist.NotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    Environment environment;
    private static final Logger log = LoggerFactory.getLogger(AddressServiceImpl.class);
    @Autowired
    private AddressRepository repository;
    private String latitude;
    private String longitude;

    @Autowired
    public AddressServiceImpl(AddressRepository repository) {
        this.repository = repository;
    }

    public AddressServiceImpl() {
    }

    @Override
    public Address save(AddressDTO addressDTO) throws Exception {
        Address address = new Address();
        try {

            Address duplicateAddress = this.findById(addressDTO.getId());
            if (duplicateAddress != null) { // Avoid a duplicated Address
                log.info("Address with id = {} found in database", address.getId());
                throw new NotFoundException("Address Id : " + (address.getId()));
            } else {
                if (addressDTO.getStreetName() != null) {
                    address.setStreetName(addressDTO.getStreetName());
                }
                if (addressDTO.getNumber() != null) {
                    address.setNumber(addressDTO.getNumber());
                }
                if (addressDTO.getNeighbourhood() != null) {
                    address.setNeighbourhood(addressDTO.getNeighbourhood());
                }
                if (addressDTO.getComplement() != null) {
                    address.setComplement(addressDTO.getComplement());
                }
                if (addressDTO.getCity() != null) {
                    address.setCity(addressDTO.getCity());
                }
                if (addressDTO.getState() != null) {
                    address.setState(addressDTO.getState());
                }
                if (addressDTO.getCountry() != null) {
                    address.setCountry(addressDTO.getCountry());
                }
                if (addressDTO.getZipCode() != null) {
                    address.setZipCode(addressDTO.getZipCode());
                }
                if ((!addressDTO.getLatitude().isEmpty() /*&& addressDTO.getLatitude() != null*/) || (!addressDTO.getLongitude().isEmpty() /*&& addressDTO.getLongitude() != null)*/)) {
                    address.setLatitude(addressDTO.getLatitude());
                    address.setLongitude(addressDTO.getLongitude());
                } else {
                    getLatitudeAndLongitude(addressDTO);
                    address.setLatitude(latitude);
                    address.setLongitude(longitude);
                }
            }
            return repository.save(address);
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());
        }
        return null;
    }


    @Override
    public List<Address> findAll() {
        List<Address> list = new ArrayList<>();
        repository.findAll().forEach(e -> list.add(e));
        return list;
    }

    @Override
    public Address findById(Long id) throws Exception {
        try {
            Address address = repository.findById(id).orElse(null);
            return address;
        } catch (Exception e) {
            log.info(e.getMessage());
        }
        return null;
    }

    @Override
    public boolean update(Long id, AddressDTO addressDTO) throws Exception {
        log.info("Address to be updated : id = {}", id);
        Address address;
        try {
            Address oldAddress = this.findById(id);
            if (oldAddress != null) {
                address = oldAddress;
                address.setStreetName(addressDTO.getStreetName());
                address.setNumber(addressDTO.getNumber());
                address.setComplement(addressDTO.getComplement());
                address.setNeighbourhood(addressDTO.getNeighbourhood());
                address.setCity(addressDTO.getCity());
                address.setState(addressDTO.getState());
                address.setCountry(addressDTO.getCountry());
                address.setZipCode(addressDTO.getZipCode());
                address.setLatitude(addressDTO.getLatitude());
                address.setLongitude(addressDTO.getLongitude());

                repository.save(address);
                return  true;
            } else {
                log.info("Address with id = {} cannot found in the database", addressDTO.getId());
                throw new NotFoundException("Unable to find this address");
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info(e.getMessage());

        }
        return false;
    }

    @Override
    public boolean delete(Long id) throws Exception {
        try{
            Address address = this.findById(id);
            if (address != null) {
                repository.delete(address);
                return true;
            } else {
                throw new NotFoundException("Object that correspond this id{} was not found");
            }
        }catch (Exception e){
            log.info(e.getMessage());
        }
        return false;
    }

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    private void getLatitudeAndLongitude(AddressDTO addressDTO) throws Exception {
        String result = null;
        String lat = null;
        String lng = null;

        String street = addressDTO.getStreetName().replace(" ", "+");
        String number = addressDTO.getNumber().toString().replace(" ", "+");
        String city = addressDTO.getCity().replace(" ", "+");
        String accessKey = environment.getProperty("google.geocode.access.key");

        HttpGet request = new HttpGet("https://maps.googleapis.com/maps/api/geocode/json?address=" + number + street + city + "&key=" + accessKey);


        try (CloseableHttpResponse response = httpClient.execute(request)) {

            System.out.println(response.getStatusLine().toString());

            HttpEntity entity = response.getEntity();
            Header headers = entity.getContentType();
            System.out.println(headers);

            if (entity != null) {
                result = EntityUtils.toString(entity);
                System.out.println(result);
            }
            String str = result.trim();
            str = str.split("location")[1].trim();
            str = str.replaceAll("\"", "");
            str = str.split("lat :")[1];
            latitude = str.split(",")[0].trim();
            lng = str.split("}, ")[0];
            lng = lng.split("},")[0];
            longitude = lng.split(": ")[1];
            System.out.println(latitude);
            System.out.println(longitude);
            
        } catch (Exception e) {
            log.info(e.getMessage());
        }

    }
}
