package ru.projects.prog_ja.model.dao.Hibernate.helpers;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import ru.projects.prog_ja.dto.commons.CommonCommentTransfer;
import ru.projects.prog_ja.model.dao.Hibernate.HibernateProblemsDAO;

public class HibernateMain {


    public static void main(String[] args) {

        new HibernateMain().run();
    }

    private void run(){

        hibernate();

    }

    private void hibernate(){
        SessionFactory sessionFactory
                = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();

        Session session = sessionFactory.getCurrentSession();
        session.beginTransaction();

        HibernateProblemsDAO problemsDAO = new HibernateProblemsDAO(sessionFactory, new AttachTagService<>(sessionFactory));

        CommonCommentTransfer comment =
                problemsDAO.addComment(12, "Небольшой комментарий от маленькой писечки", 14);


        System.out.println(comment);

        sessionFactory.getCurrentSession().getTransaction().commit();
    }
}
