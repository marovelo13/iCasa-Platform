package controllers;

import models.values.Product;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;

public class ProductController extends Controller {

	static Form<Product> productForm = form(Product.class);


	public static Result products(){
		return ok(views.html.products.products.render(Product.all()));
	}

	public static Result addProductForm(){
		return ok(views.html.products.newProduct.render(form(Product.class)));
	}

	public static Result addProduct(){
		Form<Product> filledForm = productForm.bindFromRequest();
		if (filledForm.hasErrors()){
			return badRequest(views.html.products.newProduct.render(filledForm));
		} else {
			try{
				Product.create(filledForm.get());
			}catch (Exception ex){
				return badRequest(views.html.products.newProduct.render(filledForm));
			}
			return redirect(routes.ProductController.products());
		}
	}
}