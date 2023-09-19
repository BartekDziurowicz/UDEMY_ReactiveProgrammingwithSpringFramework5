package com.reactive.Reactive.web.mappers;

import com.reactive.Reactive.domain.Customer;
import com.reactive.Reactive.web.model.CustomerDto;
import org.mapstruct.Mapper;

/**
 * Created by jt on 2019-05-25.
 */
@Mapper
public interface CustomerMapper {

    Customer customerDtoToCustomer(CustomerDto dto);

    CustomerDto customerToCustomerDto(Customer customer);
}
