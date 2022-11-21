/*
 *
 *  * Copyright (c) 2022.  - All Rights Reserved
 *  *  * Unauthorized copying or redistribution of this file in source and binary forms via any medium
 *  *  * is strictly prohibited-
 *  *  * @Author -kartiks.
 *
 */

package database.classes;

import org.glassfish.jersey.server.ResourceConfig;

public class Application extends ResourceConfig {
    public Application(){
        packages("database.webapp.classes");
    }
}
