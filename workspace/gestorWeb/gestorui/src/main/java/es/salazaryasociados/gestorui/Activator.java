/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package es.salazaryasociados.gestorui;

import java.util.ServiceLoader;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Activator implements BundleActivator {

	static Logger logger = LoggerFactory.getLogger(Activator.class);
	
    public void start(BundleContext context) {       
        ServicesProvider.SetContext(context);
        logger.info("Activando terminal. Se establece el contexto");
    }

	public void stop(BundleContext context) {
    	logger.info("Desactivando terminal.");
    }
}