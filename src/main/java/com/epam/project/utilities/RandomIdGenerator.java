package com.epam.project.utilities;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.util.Random;
//Генератор id для заказов замещающий стандартный генератор
public class RandomIdGenerator implements IdentifierGenerator {
    private Random random = new Random();
    public static int randomId;

    @Override
    public Serializable generate(SharedSessionContractImplementor s, Object o)
            throws HibernateException {
         randomId = random.nextInt(1000000);
        return randomId;
    }
}