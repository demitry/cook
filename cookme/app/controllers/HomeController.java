package controllers;

import models.Ingredient;
import play.mvc.*;
import play.data.*;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Transaction;

import play.data.*;
import static play.data.Form.*;

import models.*;

import javax.inject.Inject;
import javax.persistence.PersistenceException;

/**
 * This controller contains an action to handle HTTP requests
 * to the application's home page.
 */
public class HomeController extends Controller {

    /**
     * An action that renders an HTML page with a welcome message.
     * The configuration in the <code>routes</code> file means that
     * this method will be called when the application receives a
     * <code>GET</code> request with a path of <code>/</code>.
     */


    private FormFactory formFactory;

    @Inject
    public HomeController(FormFactory formFactory) {
        this.formFactory = formFactory;
    }

    /**
     * This result directly redirect to application home.
     */
    public Result GO_HOME = Results.redirect(
            routes.HomeController.list(0, "name", "asc", "")
    );

    public Result index() {
        return ok(views.html.index.render());
    }

    public Result hello() {
        return ok("Hello World");
    }

    public Result echo(String msg) {
        return ok("Echoing " + msg);
    }

    public Result helloRedirect() {
        return redirect(routes.HomeController.echo("HelloWorldv2"));
    }

    /**
     * Display the paginated list of computers.
     *
     * @param page   Current page number (starts from 0)
     * @param sortBy Column to be sorted
     * @param order  Sort order (either asc or desc)
     * @param filter Filter applied on ingredient names
     */
    public Result list(int page, String sortBy, String order, String filter) {
        return ok(
                views.html.Ingredients.render(
                        Ingredient.page(page, 10, sortBy, order, filter),
                        sortBy, order, filter
                )
        );
    }

    /**
     * Display the 'edit form' of a existing ingredient.
     *
     * @param id Id of the ingredient to edit
     */
    public Result edit(Long id) {
        Form<Ingredient> ingredientForm = formFactory.form(Ingredient.class).fill(
                Ingredient.find.byId(String.valueOf(id))
        );
        return ok(
                views.html.editForm.render(id, ingredientForm)
        );
    }

    /**
     * Handle the 'edit form' submission
     *
     * @param id Id of the ingredient to edit
     */
    public Result update(Long id) throws PersistenceException {
        Form<Ingredient> ingredientForm = formFactory.form(Ingredient.class).bindFromRequest();
        if (ingredientForm.hasErrors()) {
            return badRequest(views.html.editForm.render(id, ingredientForm));
        }

        Transaction txn = Ebean.beginTransaction();
        try {
            Ingredient savedComputer = Ingredient.find.byId(String.valueOf(id));
            if (savedComputer != null) {
                Ingredient newComputerData = ingredientForm.get();
                savedComputer.name = newComputerData.name;

                savedComputer.update();
                flash("success", "Ingredient " + ingredientForm.get().name + " has been updated");
                txn.commit();
            }
        } finally {
            txn.end();
        }

        return GO_HOME;
    }

    /**
     * Display the 'new ingredient form'.
     */
    public Result create() {
        Form<Ingredient> ingredientForm = formFactory.form(Ingredient.class);
        return ok(
            views.html.createForm.render(ingredientForm)
        );
    }

    /**
     * Handle the 'new ingredient form' submission
     */
    public Result save() {
        Form<Ingredient> ingredientForm = formFactory.form(Ingredient.class).bindFromRequest();
        if (ingredientForm.hasErrors()) {
            return badRequest(views.html.createForm.render(ingredientForm));
        }
        ingredientForm.get().save();
        flash("success", "Ingredient " + ingredientForm.get().name + " has been created");
        return GO_HOME;
    }

    /**
     * Handle ingredient deletion
     */
    public Result delete(Long id) {
        Ingredient.find.ref(String.valueOf(id)).delete();
        flash("success", "Ingredient has been deleted");
        return GO_HOME;
    }
}