package com.epam.project.repository;

import com.epam.project.model.OrderInfo;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;


class OrderRepositoryTest {
    @Mock
    OrderRepository orderRepository;

    @Test
    void findByEmail() {
        MockitoAnnotations.initMocks(this);
        given(orderRepository.findByEmail("admin@mail.ru")).
                willReturn(new ArrayList<OrderInfo>
                        (Arrays.asList(new OrderInfo("admin@mail.ru"))));

        Collection<OrderInfo> listOrderInfos = orderRepository.findByEmail("admin@mail.ru");
        assertThat(listOrderInfos).contains(new OrderInfo("admin@mail.ru"));
    }
}