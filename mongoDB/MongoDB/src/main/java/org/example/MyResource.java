package org.example;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Root resource (exposed at "myresource" path)
 */
@Path("myresource")
public class MyResource {

    CRUD crud = new CRUD();
    Operations ops = new Operations();
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String invalidURL(){
        return "Not valid url";
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return Museum document data will be returned as a text/plain response.
     */
    @GET
    @Path("/show")
    @Produces(MediaType.TEXT_PLAIN)
    public String showAll() {
        return crud.find();
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
            return crud.update(json);
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
        return crud.delete(id);
    }
    /**
     * Method handling HTTP GET requests. The returned object will be sent
     * to the client as "text/plain" media type.
     *
     * @return Museum document data will be returned as a text/plain response.
     */
    @GET
    @Path("/sorting/{element}")
    @Produces(MediaType.TEXT_PLAIN)
    public String sort(@PathParam("element") String element) {
        if(element == null){
            return "Required column headers";
        }
        else {
            return ops.sort(element);
        }
    }
    @GET
    @Path("/search/{element}")
    @Produces(MediaType.TEXT_PLAIN)
    public String search(@PathParam("element") String data) {
        if(data.equals("")){
            return "Search";
        }
        else {
            return ops.search(data);
        }
    }
}