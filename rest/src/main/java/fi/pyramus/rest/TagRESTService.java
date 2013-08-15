package fi.pyramus.rest;

import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.commons.lang.StringUtils;

import fi.pyramus.domainmodel.base.Tag;
import fi.pyramus.rest.controller.TagController;
import fi.pyramus.rest.tranquil.base.TagEntity;
import fi.tranquil.TranquilityBuilderFactory;

@Path("/tags")
@Produces("application/json")
@Consumes("application/json")
@Stateful
@RequestScoped
public class TagRESTService extends AbstractRESTService {
  @Inject
  private TranquilityBuilderFactory tranquilityBuilderFactory;
  @Inject
  private TagController tagController;
  
  @Path("/tags")
  @POST
  public Response createTag(TagEntity tagEntity) {
    String text = tagEntity.getText();
    if (!StringUtils.isBlank(text)) {
      return Response.ok()
          .entity(tranqualise(tagController.createTag(text)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }
  
  @Path("/tags")
  @GET
  public Response findTags() {
    List<Tag> tags = tagController.findTags();
    if (!tags.isEmpty()) {
      return Response.ok()
          .entity(tranqualise(tags))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/tags/{ID:[0-9]*}")
  @GET
  public Response findTagById(@PathParam("ID") Long id) {
    Tag tag = tagController.findTagById(id);
    if (tag != null) {
      return Response.ok()
          .entity(tranqualise(tag))
          .build();
    } else {
      return Response.status(Status.NOT_FOUND).build();
    }
  }
  
  @Path("/tags/{ID:[0-9]*}")
  @PUT
  public Response updateTagText(@PathParam("ID") Long id, TagEntity tagEntity) {
    Tag tag = tagController.findTagById(id);
    String text = tagEntity.getText();
    if(tag != null && !StringUtils.isBlank(text)) {
      return Response.ok()
          .entity(tranqualise(tagController.updateTagText(tag,text)))
          .build();
    } else {
      return Response.status(500).build();
    }
  }

  @Override
  protected TranquilityBuilderFactory getTranquilityBuilderFactory() {
    return tranquilityBuilderFactory;
  }
  

}
