/**
 * Copyright (c) 2014-2017 by the respective copyright holders.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package org.openhab.binding.blueiris.internal.control;

import org.openhab.binding.blueiris.internal.data.CamListReply;
import org.openhab.binding.blueiris.internal.data.LoginReply;

/**
 * Listener to the connection to be informed of changes from the blue iris side of things.
 * 
 * @author David Bennett - Initial contribution
 */
public interface ConnectionListener {

    void onLogin(LoginReply loginReply);

    void onCamList(CamListReply camListReply);

    void onFailedToLogin();

}
