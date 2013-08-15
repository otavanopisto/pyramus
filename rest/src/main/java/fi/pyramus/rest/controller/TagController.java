package fi.pyramus.rest.controller;

import java.util.List;

import javax.ejb.Stateless;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;

import fi.pyramus.dao.base.TagDAO;
import fi.pyramus.domainmodel.base.Tag;

@Dependent
@Stateless
public class TagController {
  @Inject
  TagDAO tagDAO;

  public Tag createTag(String text) {
    Tag tag = tagDAO.create(text);
    return tag;
  }

  public List<Tag> findTags() {
    List<Tag> tags = tagDAO.listAll();
    return tags;
  }

  public Tag findTagById(Long id) {
    Tag tag = tagDAO.findById(id);
    return tag;
  }

  public Tag updateTagText(Tag tag, String text) {
    tagDAO.updateText(tag, text);
    return tag;
  }

}
