package com.synlabs.ipsaa.view.common;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class Menu {

    private Set<MenuItem> items = new LinkedHashSet<>();


    /**
     * Add selective child menuItem to parent rather then copying all children of parent
     * @param parent
     * @param menuItem
     */
    public void add(MenuItem parent, MenuItem menuItem) {

        MenuItem addedParent = null;
        if(!items.contains(parent)){
            addedParent = new MenuItem(parent);
            items.add(addedParent);
        } else {
            for (MenuItem mainItem : items) {
                if (parent.equals(mainItem)) {
                    addedParent = mainItem;
                }
            }
        }

        if (addedParent != null) addedParent.getSubmenu().add(menuItem);

    }

    public void add(List<MenuItem> menu) {
        for (MenuItem item : menu) {
            if(items.contains(item)){
                for (MenuItem mainItem : items) {
                    if (item.equals(mainItem)) {
                        mainItem.getSubmenu().addAll(item.getSubmenu());
                        break;
                    }
                }
            } else {
                items.add(item);
            }
        }
    }


    public Set<MenuItem> getItems() {
        return items;
    }

    public void setItems(Set<MenuItem> items) {
        this.items = items;
    }
}
