package com.azuga;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Root resource (exposed at "azuga" path)
 */
@Path("azuga")
public class MyResource {

    /**
     * Method handling HTTP GET requests. This returns data in json format.
     *
     * @return String that will be returned as a text/plain response.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String showAll() {
        CrudOperations crudOperations = new CrudOperations();
        return crudOperations.select();
    }
    /**
     * Method handling HTTP PUT requests. This method update data in database.
     *
     * @return String that will be returned as a text/plain response.
     */
    @PUT
    @Path("/update-data")
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    public String update( String json){
        if(json!=null) {
            CrudOperations crudOperations = new CrudOperations();
            return crudOperations.update(json);
        }else{
            return "null";
        }
    }
    /**
     * Method handling HTTP DELETE requests. This method deletes row data from database.
     *
     * @return String that will be returned as a text/plain response.
     */
    @DELETE
    @Path("/delete/{id}")
    public Response delete(@PathParam("id") String id){
        CrudOperations crudOperations = new CrudOperations();
        return crudOperations.delete(id);
    }
    /**
     * Method handling HTTP POST requests. This method insert data in database.
     *
     * @return String that will be returned as a text/plain response.
     */
    @POST
    @Path("/insert")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Response insertData(String json){
        CrudOperations crudOperations = new CrudOperations();
        return crudOperations.insert(json,"test");
    }
}
