package com.rhtraining.projectservice01;

import java.util.List;

//import com.rhtraining.formalapi.FormalAPIVerticle;

import io.vertx.config.ConfigRetriever;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonObject;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

/**
 * Hello world!
 *
 */
public class VertxMongoVerticle extends AbstractVerticle
{
	private static final Logger LOGGER = LoggerFactory.getLogger(VertxMongoVerticle.class);
	
	public static MongoClient mongoClient = null;
	
    public static void main( String[] args )
    {    	
        Vertx vertx = Vertx.vertx();
        
        vertx.deployVerticle(new VertxMongoVerticle());
        
    }
    
    @Override
    public void start() {
    	LOGGER.info("Verticle VertxMongoVerticle Started");
    	
    	Router router = Router.router(vertx);
    	
    	router.get("/mongofind").handler(this::getAllProjects);
    	
    	JsonObject dbConfig = new JsonObject();
    	
    	dbConfig.put("connection_string", "mongodb://userHXY:04L5LOUveb3sEjRb@mongodb/projectsdb");
//    	dbConfig.put("connection_string", "mongodb://localhost:27017/MongoTest");
//    	dbConfig.put("username", "admin");
//    	dbConfig.put("password", "password");
//    	dbConfig.put("authSource", "MongoTest");
    	dbConfig.put("useObjectId", true);
    	
    	mongoClient = MongoClient.createShared(vertx, dbConfig);
    	
    	vertx.createHttpServer().requestHandler(router::accept).listen(8080);
    
    }
    
    // Get all products as array of products
    private void getAllProjects(RoutingContext routingContext) {
    	
   // 	FindOptions findOptions = new FindOptions();
   //	findOptions.setLimit(1);
    	
  //  	mongoClient.find("products", new JsonObject().put("number", "1233"), results -> {
    	mongoClient.find("projects", new JsonObject(), results -> {
    		
    		try {
    			List<JsonObject> objects = results.result();
    			
    			if (objects != null && objects.size() != 0) {
    				System.out.println("Got some data len=" + objects.size());
    				
    				JsonObject jsonResponse = new JsonObject();
    				
    				jsonResponse.put("projects", objects);
    				
    				routingContext.response()
    					.putHeader("content-type", "application/json; charset=utf-8")
    					.setStatusCode(200)
    					.end(Json.encodePrettily(jsonResponse));
    			}
    			else {
    				JsonObject jsonResponse = new JsonObject();
        			
        			jsonResponse.put("error", "No items found");
        			
        			routingContext.response()
        				.putHeader("content-type", "application/json; charset=utf-8")
        				.setStatusCode(400)
        				.end(Json.encodePrettily(jsonResponse));
    			}
    		}
    		catch(Exception e) {
    			LOGGER.info("getAllProjects Failed exception e=",e.getLocalizedMessage());
    			
    			JsonObject jsonResponse = new JsonObject();
    			
    			jsonResponse.put("error", "Exception and No items found");
    			
    			routingContext.response()
    				.putHeader("content-type", "application/json; charset=utf-8")
    				.setStatusCode(400)
    				.end(Json.encodePrettily(jsonResponse));
    			
    			
    		}
    		
    		
    	});
    	
    	
//  	JsonObject responseJson = new JsonObject();
//  	
////  	JsonArray items = new JsonArray();
//  	
////  	JsonObject firstItem = new JsonObject();
////  	firstItem.put("number", "123");
////  	firstItem.put("description", "My item 123");
////  	
////  	items.add(firstItem);
////  	
////  	JsonObject secondItem = new JsonObject();
////  	secondItem.put("number", "321");
////  	secondItem.put("description", "My item 321");
////  	
////  	items.add(secondItem);
////  	
////  	responseJson.put("products", items);
//  	
//  	Product firstItem = new Product("112233", "123", "My item 123");
//  	Product secondItem = new Product("11334455", "321", "My item 321");
//  	
//  	List<Product> products = new ArrayList<Product>();
//  	
//  	products.add(firstItem);
//  	products.add(secondItem);
//  	
//  	responseJson.put("products", products);
//  	
//  	routingContext.response()
//  		.setStatusCode(200)
//  		.putHeader("content-type", "application/json")
//  		.end(Json.encodePrettily(responseJson));
//  	
////  	routingContext.response()
////		.setStatusCode(400)
////		.putHeader("content-type", "application/json")
////		.end(new JsonObject().put("error", "Could not find all products").toString());
//  	
////  	routingContext.response()
////		.setStatusCode(400)
////		.putHeader("content-type", "application/json")
////		.end(Json.encodePrettily(new JsonObject().put("error", "Could not find all products")));
//  	
    }
    
    @Override
    public void stop() {
    	LOGGER.info("Verticle VertxMongoVerticle Stopped");    	
    }

}
