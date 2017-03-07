/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.gfx;

import javax.servlet.http.HttpServlet;

/**
 * The weather servlet serves the Html code based on the weather layouts.
 * Example layouts and icons can be downloaded from the wiki page.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WeatherServlet extends HttpServlet {
    //
    // private static final long serialVersionUID = -8254216127563270956L;
    // private static final Logger logger = LoggerFactory.getLogger(WeatherServlet.class);
    //
    // private static final String SERVLET_NAME = "/weather";
    // private static final String WEBAPP_LOCATION = "./webapps/weather-data";
    // private static final String LAYOUTS_LOCATION = WEBAPP_LOCATION + "/layouts";
    //
    // private HttpService httpService;
    //
    // public void setHttpService(HttpService httpService) {
    // this.httpService = httpService;
    // }
    //
    // public void unsetHttpService(HttpService httpService) {
    // this.httpService = null;
    // }
    //
    // /**
    // * Activates the weather servlet.
    // */
    // protected void activate() {
    // try {
    // logger.debug("Starting up weather servlet at " + SERVLET_NAME);
    //
    // Hashtable<String, String> props = new Hashtable<String, String>();
    // httpService.registerServlet(SERVLET_NAME, this, props, createHttpContext());
    //
    // } catch (Exception ex) {
    // logger.error("Error during weather servlet startup", ex);
    // }
    // }
    //
    // /**
    // * Deactivates the weather servlet.
    // */
    // protected void deactivate() {
    // httpService.unregister(SERVLET_NAME);
    // }
    //
    // /**
    // * Creates a SecureHttpContext which handles the security for this servlet.
    // */
    // private HttpContext createHttpContext() {
    // HttpContext defaultHttpContext = httpService.createDefaultHttpContext();
    // return new SecureHttpContext(defaultHttpContext, "openHAB.org");
    // }
    //
    // /**
    // * {@inheritDoc}
    // */
    // @Override
    // protected void doGet(HttpServletRequest request, HttpServletResponse response)
    // throws ServletException, IOException {
    // logger.debug("Received incoming weather request");
    //
    // String locationId = request.getParameter("locationId");
    // if (StringUtils.isBlank(locationId)) {
    // throw new ServletException("Weather locationId required, please add parameter locationId to the request");
    // }
    //
    // Weather weather = WeatherContext.getInstance().getWeather(locationId);
    // if (weather == null) {
    // throw new ServletException("Weather locationId '" + locationId + "' does not exist");
    // }
    //
    // String layout = request.getParameter("layout");
    // if (StringUtils.isBlank(layout)) {
    // throw new ServletException("Weather layout required, please add parameter layout to the request");
    // }
    //
    // layout += ".html";
    //
    // File layoutFile = new File(LAYOUTS_LOCATION + "/" + layout);
    // if (!layoutFile.exists()) {
    // throw new ServletException("File with weather layout '" + layout
    // + "' does not exist, make sure it is in the layouts folder " + LAYOUTS_LOCATION);
    // }
    //
    // WeatherTokenResolver tokenResolver = new WeatherTokenResolver(weather, locationId);
    // Enumeration<String> parameter = request.getParameterNames();
    // while (parameter.hasMoreElements()) {
    // String parameterName = parameter.nextElement();
    // tokenResolver.addParameter(parameterName, request.getParameter(parameterName));
    // }
    //
    // if (request.getParameter("iconset") == null) {
    // tokenResolver.addParameter("iconset", "colorful");
    // }
    //
    // TokenReplacingReader replReader = new TokenReplacingReader(new FileReader(layoutFile), tokenResolver);
    // IOUtils.copy(replReader, response.getOutputStream());
    // }

}
