package controller;

import java.util.Date;
import java.util.List;
import java.util.stream.Stream;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Ispit;
import model.Predispitnaobaveza;
import model.Predmet;
import utils.PersistenceUtil;

public class Controller {
	
	private static EntityManager em = null;
	private static EntityTransaction et = null;
	
	public static boolean insertSubject(Predmet p) {
		
		em = PersistenceUtil.getEntityManager();
	    et = em.getTransaction();
		
		try {
			
			et.begin();
			
			em.persist(p);
			
			em.flush();
						
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			
			et.rollback();
			return false;
			
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static List<Predmet> getSubjects() {
		
		em = PersistenceUtil.getEntityManager();
		
		String upit = "select p from Predmet p";
		
		List<Predmet> lista = em.createQuery(upit, Predmet.class).getResultList();
		
		em.close();
		
		return lista;
	}
	
	public static boolean insertColloquim(Predispitnaobaveza po) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			em.persist(po);
			
			em.flush();
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			
			et.rollback();
			return false;
			
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean connectSubjectColloquim(Predmet p, Predispitnaobaveza po) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			
			p.addPredispitnaobaveza(po);
			
			em.persist(p);
			
			em.flush();
			
			et.commit();
			
			return true;
			
		}  catch(Exception e) {
			
			et.rollback();
			return false;
			
		} finally {
			
			if(em != null)
				em.close();
		}
	}
	
	public static boolean insertExam(Ispit i) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			em.persist(i);
			
			em.flush();
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			
			et.rollback();
			return false;
			
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean connectSubjectExam(Predmet p, Ispit i) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			
			p.addIspit(i);
			
			int ocena = i.getOcena();
			
			if(ocena == 5) {
				p.setPolozen("Ne");
			} else {
				p.setPolozen("Da");
			}
			
			em.persist(p);
			
			em.flush();
			
			et.commit();
			
			return true;
			
		}  catch(Exception e) {
			
			et.rollback();
			return false;
			
		} finally {
			
			if(em != null)
				em.close();
		}
	}
	
	public static Stream<Ispit> getIspitStream() {
		
		em = PersistenceUtil.getEntityManager();
		
		String upit = "select i from Ispit i where i.ocena > 5";
		
		return em.createQuery(upit, Ispit.class)
				 .getResultList()
				 .stream();
		
	}
	
	public static boolean updateColloquim(Predispitnaobaveza po, double bodovi) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			po = em.merge(po);
			em.remove(po);
			
			po.setBrBodova(bodovi);
			
			em.persist(po);
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean updateColloquim(Predispitnaobaveza po, Date datum, double bodovi) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			po = em.merge(po);
			em.remove(po);
			
			po.setBrBodova(bodovi);
			po.setDatum(datum);
			
			em.persist(po);
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean updateExam(Ispit i, double bod, double bodoviU, int ocena) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			i = em.merge(i);
			em.remove(i);
			
			i.setBrBodova(bod);
			i.setUkupnoBodova(bodoviU);
			i.setOcena(ocena);
			
			em.persist(i);
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean updateExam(Ispit i, double bod, double bodoviU, int ocena, Date date) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			i = em.merge(i);
			em.remove(i);
			
			i.setBrBodova(bod);
			i.setUkupnoBodova(bodoviU);
			i.setOcena(ocena);
			i.setDatum(date);
			
			em.persist(i);
			
			et.commit();
			
			return true;
			
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static List<Predispitnaobaveza> getColloquiums(Predmet p) {
		
		em = PersistenceUtil.getEntityManager();
		
		String upit = "select po from Predispitnaobaveza po where po.predmet.idPredmet=:id";
		
		Query query = em.createQuery(upit);
		query.setParameter("id", p.getIdPredmet());
		
		List<Predispitnaobaveza> lista = (List<Predispitnaobaveza>) query.getResultList();
			
		em.close();
		
		return lista;
	
	}
	
	@SuppressWarnings("unchecked")
	public static List<Ispit> getExams(Predmet p) {
		
		em = PersistenceUtil.getEntityManager();
		
		String upit = "select i from Ispit i where i.predmet.idPredmet=:id";
		
		Query query = em.createQuery(upit);
		query.setParameter("id", p.getIdPredmet());
		
		List<Ispit> lista = (List<Ispit>) query.getResultList();
			
		em.close();
		
		return lista;
	
	}
	
	public static boolean changeNameSubject(Predmet p, String vr) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			em.remove(p);
			
			p.setNazPred(vr);
			
			em.persist(p);
			
			et.commit();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean changeYearSubject(Predmet p, int vr) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			em.remove(p);
			
			p.setGodina(vr);
			
			em.persist(p);
			
			et.commit();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean changeStatusSubject(Predmet p, String vr) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			em.remove(p);
			
			p.setStatus(vr);
			
			em.persist(p);
			
			et.commit();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean changeProfessorSubject(Predmet p, String vr) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			em.remove(p);
			
			p.setNazProf(vr);
			
			em.persist(p);
			
			et.commit();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}
	
	public static boolean changeESPBSubject(Predmet p, int vr) {
		
		em = PersistenceUtil.getEntityManager();
		et = em.getTransaction();
		
		try {
			
			et.begin();
			
			p = em.merge(p);
			em.remove(p);
			
			p.setBrEspb(vr);;
			
			em.persist(p);
			
			et.commit();
			
			return true;
		} catch(Exception e) {
			e.printStackTrace();
			et.rollback();
			return false;
		} finally {
			if(em != null)
				em.close();
		}
	}

}
