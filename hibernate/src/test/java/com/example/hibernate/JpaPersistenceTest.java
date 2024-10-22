package com.example.hibernate;

import jakarta.persistence.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hibernate.jpa.QueryHints;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

public class JpaPersistenceTest {

    private static final Logger LOGGER = LogManager.getLogger(JpaPersistenceTest.class);

    private static EntityManagerFactory factory;

    @BeforeAll
    static void setup() {
        factory = Persistence.createEntityManagerFactory("com.example.hibernate");
    }

    @AfterEach
    void cleanup() {
        try (EntityManager entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.createQuery("select p from Person p", Person.class).getResultList().forEach(entityManager::remove);
            entityManager.getTransaction().commit();
        }
    }

    @Test
    void test() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        List<Message> messages = readMessages();

        Assertions.assertEquals(10, messages.size());

        for (Message message : messages) {
            System.out.println(message);
        }
    }

    @Test
    void testLazyLoading() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        List<Message> messages = readMessages();

        Assertions.assertEquals(10, messages.size());

        for (Message message : messages) {
            System.out.println(message);
            System.out.println(message.getRecipient());
        }
    }

    private Person savePerson(String name) {
        LOGGER.info("saving person: " + name);

        Person person = new Person(name);

        try (EntityManager entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(person);
            entityManager.getTransaction().commit();
        }

        return person;
    }

    private void saveMessage(String text, Person person) {
        LOGGER.info("saving message: " + text);

        Message message = new Message(text);
        message.setRecipient(person);

        try (EntityManager entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();
            entityManager.persist(message);
            entityManager.getTransaction().commit();
        }
    }

    private List<Message> readMessages() {
        LOGGER.info("reading messages");

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<Message> query = entityManager.createQuery("select m from Message m", Message.class);
            return query.getResultList();
        }

    }

    @Test
    void testJoinFetching() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        List<Message> messages = readMessagesJoinRecipient();

        Assertions.assertEquals(10, messages.size());

        for (Message message : messages) {
            System.out.println(message);
            System.out.println(message.getRecipient());
        }
    }

    private List<Message> readMessagesJoinRecipient() {
        LOGGER.info("reading messages join recipient");

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<Message> query = entityManager.createQuery("select m from Message m join fetch m.recipient r", Message.class);
            return query.getResultList();
        }

    }


    @Test
    void testProjection() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        List<MessageProjection> messages = readMessageProjections();

        Assertions.assertEquals(10, messages.size());

        for (MessageProjection message : messages) {
            System.out.println(message);
        }
    }

    private List<MessageProjection> readMessageProjections() {
        LOGGER.info("reading message projections");

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<MessageProjection> query = entityManager
                    .createQuery("select new com.example.hibernate.MessageProjection(m.text, m.recipient.name) from Message m", MessageProjection.class);
            return query.getResultList();
        }

    }

    @Test
    void testDistinct() {
        Person person = savePerson("John Doe");

        for (int i = 0; i < 10; i++) {
            saveMessage("test" + i, person);
        }

        List<Person> recipients = readRecipientsFromMessage();

        Assertions.assertEquals(1, recipients.size());

        for (Person recipient : recipients) {
            System.out.println(recipient);
        }
    }

    private List<Person> readRecipientsFromMessage() {
        LOGGER.info("reading recipient from message");

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<Person> query = entityManager.createQuery("select p from Person p join fetch p.messages m", Person.class);
            return query.getResultList();
        }
    }

    @Test
    void testBatch() {
        saveManyPersons("John Doe", 10);

        List<Person> persons = readPersons();

        Assertions.assertEquals(10, persons.size());

        for (Person person : persons) {
            System.out.println(person);
        }
    }

    private void saveManyPersons(String name, int count) {
        LOGGER.info("saving many persons: " + name);


        try (EntityManager entityManager = factory.createEntityManager()) {
            entityManager.getTransaction().begin();

            for (int i = 0; i < count; i++) {
//                if (i % 5 == 0) {
//                    entityManager.flush();
//                    entityManager.clear();
//                }
                Person person = new Person(name + " " + i);
                entityManager.persist(person);
            }


            entityManager.getTransaction().commit();
        }
    }

    private List<Person> readPersons() {
        LOGGER.info("reading persons");

        try (EntityManager entityManager = factory.createEntityManager()) {
            TypedQuery<Person> query = entityManager.createQuery("select p from Person p", Person.class);
            return query.getResultList();
        }
    }

}
