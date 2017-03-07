/**
 * Copyright (c) 2010-2016 by the respective copyright holders.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.weather.internal.provider;

import org.openhab.binding.weather.internal.model.ProviderName;
import org.openhab.binding.weather.internal.parser.CommonIdHandler;
import org.openhab.binding.weather.internal.parser.JsonWeatherParser;

/**
 * WorldWeatherOnline weather provider.
 *
 * @author Gerhard Riegler
 * @since 1.6.0
 */
public class WorldWeatherOnlineProvider extends AbstractWeatherProvider {
    private static final String URL = "http://api.worldweatheronline.com/free/v2/weather.ashx?key=[API_KEY]&q=[LATITUDE],[LONGITUDE]&extra=localObsTime&num_of_days=5&format=json&lang=[LANGUAGE]";

    public WorldWeatherOnlineProvider(CommonIdHandler commonIdHandler) {
        super(new JsonWeatherParser(commonIdHandler));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProviderName getProviderName() {
        return ProviderName.WorldWeatherOnline;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getWeatherUrl() {
        return URL;
    }

}
