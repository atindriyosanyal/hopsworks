/*
 * Copyright (C) 2013 - 2018, Logical Clocks AB and RISE SICS AB. All rights reserved
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit
 * persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL  THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR  OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 *
 */

package io.hops.hopsworks.common.dao.tfserving;

import io.hops.hopsworks.common.dao.project.Project;
import io.hops.hopsworks.common.dao.tensorflow.TensorflowFacade;
import io.hops.hopsworks.common.metadata.exception.DatabaseException;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.logging.Logger;

@Stateless
public class TfServingFacade {
  private final static Logger LOGGER = Logger.getLogger(TensorflowFacade.class.getName());

  @PersistenceContext(unitName = "kthfsPU")
  private EntityManager em;

  protected EntityManager getEntityManager() {
    return em;
  }

  public TfServingFacade() throws Exception {

  }

  public void persist(TfServing tfServing) throws DatabaseException {
    try {
      em.persist(tfServing);
    } catch (ConstraintViolationException cve) {
      throw new DatabaseException("You can not create a serving with the same name as an existing one");
    }
  }

  public List<TfServing> findForProject(Project project) {
    TypedQuery<TfServing> q = em.createNamedQuery("TfServing.findByProject", TfServing.class);
    q.setParameter("project", project);
    return q.getResultList();
  }

  public boolean updateServingVersion(TfServing tfServing) throws DatabaseException {
    boolean status = false;
    try {
      TypedQuery<TfServing> q = em.createNamedQuery("TfServing.updateModelVersion", TfServing.class);
      q.setParameter("id", tfServing.getId());
      q.setParameter("version", tfServing.getVersion());
      q.setParameter("hdfsModelPath", tfServing.getHdfsModelPath());

      int result = q.executeUpdate();
      if (result == 1) {
        status = true;
      }
    } catch (SecurityException | IllegalArgumentException ex) {
      throw new DatabaseException("Could not update serving  ", ex);
    }
    return status;
  }

  public void remove(TfServing tfServing) throws DatabaseException {
    try {
      TfServing managedTfServing = em.find(TfServing.class, tfServing.getId());
      em.remove(em.merge(managedTfServing));
      em.flush();
    } catch (SecurityException | IllegalStateException ex) {
      throw new DatabaseException("Could not delete serving " + tfServing.getId(), ex);
    }
  }

  public TfServing findById(Integer id) {
    return em.find(TfServing.class, id);
  }

  public boolean updateRunningState(TfServing tfServing) throws DatabaseException {
    boolean status = false;
    try {
      TypedQuery<TfServing> q = em.createNamedQuery("TfServing.updateRunningState", TfServing.class);
      q.setParameter("id", tfServing.getId());
      q.setParameter("pid", tfServing.getPid());
      q.setParameter("port", tfServing.getPort());
      q.setParameter("hostIp", tfServing.getHostIp());
      q.setParameter("status", tfServing.getStatus());

      int result = q.executeUpdate();
      if (result == 1) {
        status = true;
      }
    } catch (SecurityException | IllegalArgumentException ex) {
      throw new DatabaseException("Could not update serving  ", ex);
    }
    return status;
  }
}