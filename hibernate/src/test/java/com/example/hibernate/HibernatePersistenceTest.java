package com.example.hibernate;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.query.SelectionQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class HibernatePersistenceTest {

    private static final Logger LOGGER = LogManager.getLogger(HibernatePersistenceTest.class);

    private static SessionFactory factory;

    @BeforeAll
    static void setup() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder().build();
        try {
            factory = new MetadataSources(registry).addAnnotatedClass(Message.class).addAnnotatedClass(Person.class).buildMetadata().buildSessionFactory();
        } catch (Exception e) {
            StandardServiceRegistryBuilder.destroy(registry);
        }
    }

    @Test
    void test() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        {
            List<Message> messages = readMessages("test2");
        }
            List<Message> messages = readMessages("test2");

            Assertions.assertEquals(1, messages.size());

            for (Message message : messages) {
                System.out.println(message);
        }
    }

    private Person savePerson(String name) {
        LOGGER.info("saving person: " + name);

        Person person = new Person(name);

        factory.inTransaction(session -> {
            session.persist(person);
        });

        return person;
    }

    private void saveMessage(String text, Person recipient) {
        LOGGER.info("saving message: " + text);

        Message message = new Message(text);
        message.setRecipient(recipient);

        factory.inTransaction(session -> {
            session.persist(message);
        });
    }

    private List<Message> readMessages(String text) {
        LOGGER.info("reading messages");

        return factory.fromTransaction(session -> {
            SelectionQuery<Message> query = session.createQuery("select m from Message m where text = :text", Message.class);
            query.setParameter("text", text);
            query.setCacheable(true);
            return query.getResultList();
        });

    }
}
