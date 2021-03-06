package com.excilys.librarymanager.service;

import com.excilys.librarymanager.exception.ServiceException;
import com.excilys.librarymanager.exception.DaoException;

import com.excilys.librarymanager.model.Emprunt;
import com.excilys.librarymanager.model.Livre;
import com.excilys.librarymanager.model.Membre;
import com.excilys.librarymanager.model.Abonnement;

import com.excilys.librarymanager.dao.EmpruntDaoImpl;

import java.util.ArrayList;
import java.util.List;

import java.time.LocalDate;

public class EmpruntServiceImpl implements EmpruntService
{
    private static EmpruntServiceImpl instance;

	private EmpruntServiceImpl() {}

	public static EmpruntServiceImpl getInstance() {
		if (instance == null) {
			instance = new EmpruntServiceImpl();
		}
		return instance;
    }
    
    /**
	 * @brief Service pour avoir la liste des emprunts
	 */
    public List<Emprunt> getList() throws ServiceException
    {
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            return dao.getList();
        }
        catch(DaoException error)
        {
            throw new ServiceException("Erreur : Service emprunt getList");
        }
    }

    /**
	 * @brief Service pour avoir la liste des emprunts en cours
	 */
    public List<Emprunt> getListCurrent() throws ServiceException
    {
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            return dao.getListCurrent();
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt liste emprunts en cours");
        }

    }

    /**
	 * @brief Service pour avoir la liste des emprunts en cours d'un membre
	 */
    public List<Emprunt> getListCurrentByMembre(int idMembre) throws ServiceException
    {
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            return dao.getListCurrentByMembre(idMembre);
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt liste emprunts en cours d'un membre "+idMembre);
        }
    }

    /**
	 * @brief Service pour avoir la liste des emprunts en cours d'un livre
	 */
    public List<Emprunt> getListCurrentByLivre(int idLivre) throws ServiceException
    {
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            return dao.getListCurrentByLivre(idLivre);
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt liste emprunts en cours d'un livre "+idLivre);
        }
    }

    /**
	 * @brief Service pour avoir un emprunt par id du livre
	 */
    public Emprunt getById(int id) throws ServiceException
    {
        if(id<0)
        {
            throw new ServiceException("Erreur : Service recuperer un emprunt par id : ID INVALIDE");
        }
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            Emprunt emprunt = dao.getById(id); //idLivre
            if (emprunt == null)
            {
                throw new ServiceException("Erreur : Service recuperer un emprunt par id : EMRPUNT NON TROUVE");
            }
            return emprunt;
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt recuperer emprunt par id "+id);
        }
    }

    /**
	 * @brief Service pour creer un emprunt
	 */
    public void create(int idMembre, int idLivre, LocalDate dateEmprunt) throws ServiceException {
        if(idMembre<0)
        {
            throw new ServiceException("Erreur : Service creer un emprunt : ID MEMBRE INVALIDE");
        }
        if(idLivre<0)
        {
            throw new ServiceException("Erreur : Service creer un emprunt : ID LIVRE INVALIDE");
        }
        if(dateEmprunt==null)
        {
            throw new ServiceException("Erreur : Service creer un emprunt : DATE EMPRUNT NULLE");
        }
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            dao.create(idMembre,idLivre,dateEmprunt);
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt creation de l'emprunt du livre "+idLivre+ "par le membre "+idMembre+" le "+dateEmprunt);
        }
    }

    /**
	 * @brief Service pour rendre un livre
	 */
    public void returnBook(int idEmprunt) throws ServiceException
    {
        if(idEmprunt<0)
        {
            throw new ServiceException("Erreur : Service rendre un livre : ID EMPRUNT INVALIDE");
        }
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            Emprunt emprunt = dao.getById(idEmprunt);
            if(emprunt==null)
            {
                throw new ServiceException("Erreur : Service rendre un livre : EMPRUNT NON TROUVE");
            }
            emprunt.setDateRetour(LocalDate.now());
            dao.update(emprunt);
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt rendre un livre, emprunt "+idEmprunt);
        }
    }

    /**
	 * @brief Service pour compter tous les emprunts
	 */
    public int count() throws ServiceException
    {
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            int i = dao.count();
            return i ;
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt compter les emprunts");
        }
    }

    /**
	 * @brief Service pour savoir si le livre est disponible
	 */
    // on suppose qu'il n'y a qu'un seul exemplaire de chaque livre
    public boolean isLivreDispo(int idLivre) throws ServiceException
    {
        if(idLivre<0)
        {
            throw new ServiceException("Erreur : Service si livre dispo : ID LIVRE INVALIDE");
        }
        try{
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            // renvoie listes des emprunts en cours
            List<Emprunt> emprunts = dao.getListCurrentByLivre(idLivre);
            return emprunts.isEmpty();
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt si livre dispo");
        }
    }

    /**
	 * @brief Service pour savoir si l'emprunt est possible
	 */
    public boolean isEmpruntPossible(Membre membre) throws ServiceException
    {
        if(membre==null)
        {
            throw new ServiceException("Erreur : Service si emprunt possible : MEMBRE NUL");
        }
        try{
            int nbLivres ;
            switch(membre.getAbonnement())
            {
                case BASIC:
                    nbLivres = 2;
                    break;
                case PREMIUM:
                    nbLivres = 5;
                    break;
                case VIP:
                    nbLivres = 20;
                    break;
                default:
                    throw new ServiceException("Erreur : Service si emprunt possible : ABONNEMENT INVALIDE");
            }
            EmpruntDaoImpl dao = EmpruntDaoImpl.getInstance();
            int nbEmprunts = dao.getListCurrentByMembre(membre.getKey()).size();
            return (nbEmprunts<nbLivres);
        } catch (DaoException error)
        {
            throw new ServiceException("Erreur : Service Emprunt si emprunt possible");
        }
    }
}