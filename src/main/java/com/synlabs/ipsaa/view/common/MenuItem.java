package com.synlabs.ipsaa.view.common;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.Transient;
import java.util.LinkedHashSet;
import java.util.Set;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class MenuItem
{

  private String link;
  private String label;
  private String icon;
  private String state;

  @JsonIgnore
  private String  bgcolor;
  private boolean divider;
  private int     seq;

  private String p;

  private Set<MenuItem> submenu = new LinkedHashSet<>();

  @Transient
  @JsonIgnore
  private String parent;

  public MenuItem()
  {

  }

  public MenuItem(MenuItem copy)
  {
    this.label = copy.getLabel();
    this.link = copy.getLink();
    this.icon = copy.getIcon();
    this.state = copy.getState();
    this.divider = copy.isDivider();
    this.p = copy.getP();
    this.seq = copy.getSeq();
  }

  public String getLink()
  {
    return link;
  }

  public void setLink(String link)
  {
    this.link = link;
  }

  public String getLabel()
  {
    return label;
  }

  public void setLabel(String label)
  {
    this.label = label;
  }

  public Set<MenuItem> getSubmenu()
  {
    return submenu;
  }

  public void setSubmenu(Set<MenuItem> submenu)
  {
    this.submenu = submenu;
  }

  public boolean isDivider()
  {
    return divider;
  }

  public void setDivider(boolean divider)
  {
    this.divider = divider;
  }

  public void addSubMenu(MenuItem item)
  {
    submenu.add(item);
  }

  public String getIcon()
  {
    return icon;
  }

  public void setIcon(String icon)
  {
    this.icon = icon;
  }

  public String getState()
  {
    return state;
  }

  public void setState(String state)
  {
    this.state = state;
  }

  public int getSeq()
  {
    return seq;
  }

  public void setSeq(int seq)
  {
    this.seq = seq;
  }

  public String getParent()
  {
    return parent;
  }

  public void setParent(String parent)
  {
    this.parent = parent;
  }

  public String getBgcolor()
  {
    return bgcolor;
  }

  public void setBgcolor(String bgcolor)
  {
    this.bgcolor = bgcolor;
  }

  public String getP()
  {
    return p;
  }

  public void setP(String p)
  {
    this.p = p;
  }

  @Override
  public String toString()
  {
    return "MenuItem{" +
        "link='" + link + '\'' +
        ", label='" + label + '\'' +
        ", icon='" + icon + '\'' +
        ", submenu=" + submenu +
        '}';
  }

  @Override
  public boolean equals(Object o)
  {
    if (this == o)
    {
      return true;
    }
    if (o == null || getClass() != o.getClass())
    {
      return false;
    }
    MenuItem menuItem = (MenuItem) o;
    return divider == menuItem.divider &&
        (link != null ? link.equals(menuItem.link) : menuItem.link == null) && (label != null ?
                                                                                                                  label.equals(menuItem.label) :
                                                                                                                  menuItem.label == null);
  }

  @Override
  public int hashCode()
  {
    int result = link != null ? link.hashCode() : 0;
    result = 31 * result + (label != null ? label.hashCode() : 0);
    result = 31 * result + (divider ? 1 : 0);
    return result;
  }
}
