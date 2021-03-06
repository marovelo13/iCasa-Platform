package controllers.api;

import models.User;
import models.values.Order;
import models.values.Product;
import models.values.ProductPrice;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.ArrayNode;
import org.codehaus.jackson.node.ObjectNode;
import play.data.DynamicForm;
import play.data.Form;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;
import securesocial.core.Identity;
import securesocial.core.java.SecureSocial;

import java.util.List;

public class ProductREST extends Controller {

	static Form<Product> productForm = form(Product.class);

	private static final String TOP_NUMBER = "topNumber";
	
	private static final String PAGE = "page";
	
	private static final String PRODUCTS_PER_PAGE = "productsPerPage";

    @SecureSocial.SecuredAction
    public static Result products(){
        System.out.println("Getting products");
        DynamicForm requestData = form().bindFromRequest();
        System.out.println("get params: " + requestData.toString());
        if (requestData.get(TOP_NUMBER) != null) {
            System.out.println("Getting top products");
            return topProducts(requestData.get(TOP_NUMBER));
        } else if (requestData.get(PAGE) != null){
            System.out.println("Getting products per page");
			return productsPerPage(requestData.get(PAGE), requestData.get(PRODUCTS_PER_PAGE));
		}
		List<Product> allProducts = null;
		allProducts = Product.all();
		ArrayNode products = Json.newObject().arrayNode();
		for (Product product : allProducts) {
			products.add(Product.toJson(product));
		}
		return ok(products);
	}

    @SecureSocial.SecuredAction
    public static Result buyProduct(String id){
        Identity socialUser = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        User user = User.getUserByUserId(socialUser.id().id());
        Product toBuy = Product.find.byId(id);
        user.buyProduct(toBuy);

        return ok();
    }
    @SecureSocial.SecuredAction
    public static Result topProducts(String topNumber){
		List<Product> allProducts = null;
		int top = 10;
		try{
			top = Integer.valueOf(topNumber);
		}catch(Exception ex){
			top = 10;
		}
		allProducts = Product.getTopProducts(top);
		ArrayNode products = Json.newObject().arrayNode();
		for (Product product : allProducts) {
			products.add(Product.toJson(product));
		}
		return ok(products);
	}
    @SecureSocial.SecuredAction
    public static Result ownedProducts(){
        Identity socialUser = (Identity) ctx().args.get(SecureSocial.USER_KEY);
        User user = User.getUserByUserId(socialUser.id().id());
        List<Order> buy = user.orders;
        List<Product> allProducts = null;

        allProducts = user.getOwnedProducts();
        ArrayNode products = Json.newObject().arrayNode();
        for (Product product : allProducts) {
            products.add(Product.toJson(product));
        }
        return ok(products);
    }
    @SecureSocial.SecuredAction
    public static Result productsPerPage(String page, String productsPerPage){
		List<Product> allproducts = null;
		int _page;
		int _productsPerPage;
		try{
			_page = Integer.valueOf(page);
			_productsPerPage = Integer.valueOf(productsPerPage);
		}catch(Exception ex){
			ex.printStackTrace();
			_page = 1;
			_productsPerPage = 10;
		}
		allproducts = Product.getPageProducts(_productsPerPage, _page);
		int totalPages = Product.getPageCount(_productsPerPage);
		ObjectNode returningJSONObject = Json.newObject();
		returningJSONObject.put("totalPages", totalPages);
		ArrayNode products = Json.newObject().arrayNode();
		for (Product product : allproducts) {
			products.add(Product.toJson(product));
		}
		returningJSONObject.put("products", products);
		return ok(returningJSONObject);
	}

	
	public static Result addProduct(){
		System.out.println("hello add product  ");
		System.out.println("aDD PRODUCT " + request().body());
		JsonNode body = request().body().asJson();
		//System.out.println("aDD PRODUCT " + body.get(0));
		
		Form<Product> filledForm = productForm.bindFromRequest();
		System.out.println(filledForm);
		if (filledForm.hasErrors()){
			return badRequest(views.html.products.newProduct.render(filledForm));
		} else {
			try{
				Product.create(filledForm.get());
			}catch (Exception ex){
				return badRequest(views.html.products.newProduct.render(filledForm));
			}
			return ok();
		}
	}
}