package org.jboss.examples.ticketmonster.rest;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.OptimisticLockException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import org.jboss.examples.ticketmonster.rest.dto.MediaItemDTO;
import org.jboss.examples.ticketmonster.model.MediaItem;

/**
 * 
 */
@Stateless
@Path("/mediaitems")
public class MediaItemEndpoint
{
   @PersistenceContext(unitName = "primary")
   private EntityManager em;


   @GET
   @Path("/{id:[0-9][0-9]*}")
   @Produces("application/json")
   public Response findById(@PathParam("id") Long id)
   {
      TypedQuery<MediaItem> findByIdQuery = em.createQuery("SELECT DISTINCT m FROM MediaItem m WHERE m.id = :entityId ORDER BY m.id", MediaItem.class);
      findByIdQuery.setParameter("entityId", id);
      MediaItem entity;
      try
      {
         entity = findByIdQuery.getSingleResult();
      }
      catch (NoResultException nre)
      {
         entity = null;
      }
      if (entity == null)
      {
         return Response.status(Status.NOT_FOUND).build();
      }
      MediaItemDTO dto = new MediaItemDTO(entity);
      return Response.ok(dto).build();
   }

   @GET
   @Produces("application/json")
   public List<MediaItemDTO> listAll(@QueryParam("start") Integer startPosition, @QueryParam("max") Integer maxResult)
   {
      TypedQuery<MediaItem> findAllQuery = em.createQuery("SELECT DISTINCT m FROM MediaItem m ORDER BY m.id", MediaItem.class);
      if (startPosition != null)
      {
         findAllQuery.setFirstResult(startPosition);
      }
      if (maxResult != null)
      {
         findAllQuery.setMaxResults(maxResult);
      }
      final List<MediaItem> searchResults = findAllQuery.getResultList();
      final List<MediaItemDTO> results = new ArrayList<MediaItemDTO>();
      for (MediaItem searchResult : searchResults)
      {
         MediaItemDTO dto = new MediaItemDTO(searchResult);
         results.add(dto);
      }
      return results;
   }


}
