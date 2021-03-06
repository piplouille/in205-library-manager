package com.excilys.librarymanager.service;

import java.sql.SQLException;

import com.excilys.librarymanager.model.Membre;
import com.excilys.librarymanager.model.Abonnement;

import com.excilys.librarymanager.dao.MembreDaoImpl;
import com.excilys.librarymanager.dao.EmpruntDaoImpl;

import java.util.ArrayList;
import java.util.List;

import com.excilys.librarymanager.exception.ServiceException;
import com.excilys.librarymanager.exception.DaoException;

public class MembreServiceImpl implements MembreService {
    // Singleton
    private static MembreServiceImpl instance;

	private MembreServiceImpl() {
	};

	public static MembreServiceImpl getInstance() {
		if (instance == null) {
			instance = new MembreServiceImpl();
		}
		return instance;
    }

    /**
     * @brief Récupère la liste de tous les membres
     */
    public List<Membre> getList() throws ServiceException {
        MembreDaoImpl dao = MembreDaoImpl.getInstance();
        List<Membre> liste = null;
        try {
            liste = dao.getList();
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
        return liste;
    }

    /**
     * @brief Récupère la liste des membres pouvant faire un emprunt
     */
    public List<Membre> getListMembreEmpruntPossible() throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        EmpruntServiceImpl service_emprunt = EmpruntServiceImpl.getInstance();
        List<Membre> possible = new ArrayList<Membre>();

        try {
            List<Membre> membres = dao_membre.getList();
            // On teste chaque membre selon abonnement
            for (int i = 0 ; i < membres.size() ; i++) {
                if (service_emprunt.isEmpruntPossible(membres.get(i))) {
                    possible.add(membres.get(i));
                }
            }
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
        return possible;
    }

    /**
     * @brief Récupère un membre par son id
     */
    public Membre getById(int id) throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        Membre membre = null;
        try {
            membre = dao_membre.getById(id);
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
        return membre;
    }

    /**
     * @brief Ajoute un membre à la base de données
     */
    public int create(String nom, String prenom, String adresse, String email, String telephone) throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        int id = -1;
        try {
            id = dao_membre.create(nom, prenom, adresse, email, telephone);
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
        return id;
    }

    /**
     * @brief Update un membre suivant le pattern en argument
     */
    public void update(Membre membre) throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        try {
            dao_membre.update(membre);
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
    }

    /**
     * @brief Delete un membre avec l'id en argument
     */
    public void delete(int id) throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        try {
            dao_membre.delete(id);
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
    }

    /**
     * @brief Return le nombre de membres dans la BDD
     */
    public int count() throws ServiceException {
        MembreDaoImpl dao_membre = MembreDaoImpl.getInstance();
        int nombre = -1;
        try {
            nombre = dao_membre.count();
        }
        catch (DaoException e) {
            System.out.println("Exception Message " + e.getLocalizedMessage());
        }
        return nombre;
    }
}
