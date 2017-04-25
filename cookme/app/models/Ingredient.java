package models;

import java.util.*;
import javax.persistence.*;

import org.springframework.ui.Model;
import play.db.ebean.*;
import com.avaje.ebean.Model.*;
//import play.db.ebean.Model;
import javax.persistence.MappedSuperclass;
import play.data.format.*;
import play.data.validation.*;
import com.avaje.ebean.*;

@Entity
public class Ingredient extends com.avaje.ebean.Model {
    @Id
    @Constraints.Min(10)
    public String id;

    @Constraints.Required
    public String name;

    public static com.avaje.ebean.Model.Finder<String,Ingredient> find
            = new com.avaje.ebean.Model.Finder<String,Ingredient>(String.class, Ingredient.class);

    public static Map<String,String> options() {

        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();

        for(Ingredient c: Ingredient.find.orderBy("id").findList()) {
            options.put(c.id, c.name);
        }

        System.out.println(options);
        return options;
    }

    /**
     * Return a paged list of ingredients
     *
     * @param page Page to display
     * @param pageSize Number of computers per page
     * @param sortBy ingredient property used for sorting
     * @param order Sort order (either or asc or desc)
     * @param filter Filter applied on the name column
     */
    public static PagedList<Ingredient> page(int page, int pageSize, String sortBy, String order, String filter) {
        return
                find.where()
                        .ilike("name", "%" + filter + "%")
                        .orderBy(sortBy + " " + order)
                        .findPagedList(page, pageSize);
    }
}
